package edu.colorado.cires.pace.translator.converter;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.translator.TimeTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;

final class ConversionUtils {
  
  private static <T> T transformedPropertyFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException, Function<String, T> transform, String errorMessage) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, propertyName);
    String property = stringFromProperty(valueWithColumnNumber);
    
    if (property == null) {
      return null;
    }
    
    try {
      return transform.apply(property);
    } catch (Throwable e) {
      runtimeException.addSuppressed(new FieldException(
          propertyName, errorMessage, valueWithColumnNumber.column(), row
      ));
      return null;
    }
  } 
  
  public static UUID uuidFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, UUID::fromString, "Invalid UUID format");
  }
  
  public static Float floatFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, Float::parseFloat, "Invalid float format");
  }
  
  public static Integer integerFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, Integer::parseInt, "Invalid integer format");
  }
  
  public static Path pathFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, Paths::get, "Invalid path format");
  }
  
  public static LocalDate localDateFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, LocalDate::parse, "Invalid date format");
  }
  
  public static LocalDateTime localDateTimeFromMap(Map<String, ValueWithColumnNumber> properties, TimeTranslator timeTranslator, int row, RuntimeException runtimeException) {
    if (timeTranslator instanceof DefaultTimeTranslator defaultTimeTranslator) {
      return transformedPropertyFromMap(properties, defaultTimeTranslator.getTime(), row, runtimeException, LocalDateTime::parse, "Invalid date time format");
    } else if (timeTranslator instanceof DateTimeSeparatedTimeTranslator dateTimeSeparatedTimeTranslator) {
      return localDateTimeFromMap(properties, dateTimeSeparatedTimeTranslator, row, runtimeException);
    }
    return null;
  }

  private static LocalDateTime localDateTimeFromMap(Map<String, ValueWithColumnNumber> properties, DateTimeSeparatedTimeTranslator timeTranslator, int row, RuntimeException runtimeException) {
    LocalDate date = localDateFromMap(properties, timeTranslator.getDate(), row, runtimeException);
    if (date == null) {
      return null;
    }
    LocalTime time = transformedPropertyFromMap(properties, timeTranslator.getTime(), row, runtimeException, LocalTime::parse, "Invalid time format");
    if (time == null) {
      return date.atTime(0, 0);
    }
    return date.atTime(time);
  }
  
  public static ValueWithColumnNumber propertyFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    ValueWithColumnNumber valueWithColumnNumber = properties.get(propertyName);
    
    if (valueWithColumnNumber == null) {
      return new ValueWithColumnNumber(
          Optional.empty(),
          null
      );
    }
    
    return new ValueWithColumnNumber(
        valueWithColumnNumber.value().filter(string -> !StringUtils.isBlank(string)).map(String::trim),
        valueWithColumnNumber.column()
    );
  }
  
  public static String stringFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    return stringFromProperty(
        propertyFromMap(properties, propertyName)
    );
  }
  
  public static String stringFromProperty(ValueWithColumnNumber value) {
    return value.value()
        .map(string -> StringUtils.isBlank(string) ? null : string.trim())
        .orElse(null);
  }
  
  private static <O extends ObjectWithUniqueField> O objectFromUniqueField(String uniqueFieldPropertyName, String uniqueFieldValue, CRUDRepository<O> repository, int row, int column, RuntimeException runtimeException) {
    try {
      return repository.getByUniqueField(uniqueFieldValue);
    } catch (DatastoreException | NotFoundException e) {
      runtimeException.addSuppressed(new FieldException(
          uniqueFieldPropertyName, e.getMessage(), column, row
      ));
      return null;
    }
  }
  
  public static <O extends ObjectWithUniqueField> O objectFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, CRUDRepository<O> repository, int row, RuntimeException runtimeException) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, propertyName);
    String property = stringFromProperty(valueWithColumnNumber);
    
    if (property == null) {
      return null;
    }
    
    return objectFromUniqueField(propertyName, property, repository, row, valueWithColumnNumber.column(), runtimeException);
  }
  
  public static <O extends ObjectWithUniqueField> List<O> delimitedObjectsFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException, CRUDRepository<O> repository) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, propertyName);
    
    String delimitedValues = stringFromProperty(valueWithColumnNumber);
    
    if (delimitedValues == null) {
      return Collections.emptyList();
    }
    
    return Arrays.stream(delimitedValues.split(";"))
        .map(string -> objectFromUniqueField(propertyName, string.trim(), repository, row, valueWithColumnNumber.column(), runtimeException))
        .filter(Objects::nonNull)
        .toList();
  }


}
