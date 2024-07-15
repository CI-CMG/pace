package edu.colorado.cires.pace.translator.converter;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.translator.DateTranslator;
import edu.colorado.cires.pace.data.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.translator.TimeTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.translator.FieldException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
  
  public static Double doubleFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, Double::parseDouble, "Invalid double format");
  }
  
  public static Integer integerFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, Integer::parseInt, "Invalid integer format");
  }
  
  public static Path pathFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, propertyName, row, runtimeException, Paths::get, "Invalid path format");
  }
  
  public static LocalDate localDateFromMap(Map<String, ValueWithColumnNumber> properties, DateTranslator dateTranslator, int row, RuntimeException runtimeException) {
    return parseLocalDate(properties, dateTranslator, row, runtimeException);
  }
  
  private static LocalDate parseLocalDate(Map<String, ValueWithColumnNumber> properties, DateTranslator dateTranslator, int row, RuntimeException runtimeException) {
    ValueWithColumnNumber timeZone = propertyFromMap(properties, dateTranslator.getTimeZone());
    ValueWithColumnNumber date = propertyFromMap(properties, dateTranslator.getDate());
    
    String zoneValue = stringFromProperty(timeZone);
    String inputValue = stringFromProperty(date);
    
    if (zoneValue == null) {
      return null;
    }

    DateTimeFormatter dateTimeFormatter;
    
    try {
      dateTimeFormatter = new DateTimeFormatterBuilder()
          .append(DateTimeFormatter.ofPattern(
              "[yyyy-MM-dd'T'HH:mm:ssZ]" +
                  "[yyyy-MM-dd'T'HH:mm:ss]" +
                  "[yyyy-MM-dd HH:mm:ss]" +
                  "[yyyy-MM-dd]" +
                  "[d/M/yyyy HH:mm:ss]" +
                  "[d/M/yy HH:mm:ss]" +
                  "[d/M/yyyy]" +
                  "[d/M/yy]"
          )).toFormatter()
          .withZone(ZoneId.of(zoneValue));
    } catch (Throwable e) {
      runtimeException.addSuppressed(new FieldException(
          dateTranslator.getTimeZone(), "Invalid time zone", timeZone.column(), row
      ));
      return null;
    }
    
    if (inputValue == null) {
      return null;
    }
    try {
      return LocalDate.parse(inputValue, dateTimeFormatter);
    } catch (Throwable e) {
      runtimeException.addSuppressed(new FieldException(
          dateTranslator.getDate(), "Invalid date format", date.column(), row 
      ));
      return null;
    }
  }
  
  public static LocalDateTime localDateTimeFromMap(Map<String, ValueWithColumnNumber> properties, TimeTranslator timeTranslator, int row, RuntimeException runtimeException) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, timeTranslator.getTimeZone());
    String timeZone = valueWithColumnNumber.value().orElse(null);
    if (timeZone == null) {
      return null;
    }
    if (timeTranslator instanceof DefaultTimeTranslator defaultTimeTranslator) {
      DateTimeFormatter dateTimeFormatter;
      try {
        dateTimeFormatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern(
                "[yyyy-MM-dd'T'HH:mm:ss]" +
                    "[yyyy-MM-dd HH:mm:ss]" +
                    "[d/M/yyyy HH:mm:ss]" +
                    "[d/M/yy HH:mm:ss]"
            )).toFormatter()
            .withZone(ZoneId.of(timeZone));
      } catch (Exception e) {
        runtimeException.addSuppressed(new FieldException(
            timeTranslator.getTimeZone(), "Invalid time zone", valueWithColumnNumber.column(), row
        ));
        return null;
      }
      return transformedPropertyFromMap(properties, defaultTimeTranslator.getTime(), row, runtimeException, (s) -> parseLocalDateTime(s, dateTimeFormatter), "Invalid date time format");
    } else if (timeTranslator instanceof DateTimeSeparatedTimeTranslator dateTimeSeparatedTimeTranslator) {
      return localDateTimeFromMap(properties, dateTimeSeparatedTimeTranslator, row, runtimeException);
    }
    return null;
  }

  // ACT
  // AET
  // AGT
  // ART
  // AST
  // BET
  // BST
  // CAT
  // CNT
  // CST
  // CTT
  // EAT
  // ECT
  // IET
  // IST
  // JST
  // MIT
  // NET
  // NST
  // PLT
  // PNT
  // PRT
  // PST
  // SST
  // VST
  // EST
  // MST
  // HST
  // UTC
  private static LocalDateTime parseLocalDateTime(String input, DateTimeFormatter dateTimeFormatter) {
    return LocalDateTime.parse(input, dateTimeFormatter);
  }
  
  private static LocalTime parseLocalTime(String input, String zoneId) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ofPattern("HH:mm:ss"))
        .toFormatter()
        .withZone(ZoneId.of(zoneId));
    return LocalTime.parse(input, formatter);
  }

  private static LocalDateTime localDateTimeFromMap(Map<String, ValueWithColumnNumber> properties, DateTimeSeparatedTimeTranslator timeTranslator, int row, RuntimeException runtimeException) {
    String zoneId = stringFromMap(properties, timeTranslator.getTimeZone());
    
    LocalDate date = parseLocalDate(
        properties,
        timeTranslator,
        row,
        runtimeException
    );
    if (date == null) {
      return null;
    }
    LocalTime time = transformedPropertyFromMap(properties, timeTranslator.getTime(), row, runtimeException, (s) -> parseLocalTime(s, zoneId), "Invalid time format");
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
  
  public static Double latitudeFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    
    String latitudeString = stringFromMap(properties, propertyName);

    Matcher matcher = Pattern.compile("^(?<degrees>\\d{1,2}[°Dd])\\s*(?<minutes>\\d{1,2}['Mm])\\s*(?<seconds>\\d{1,2}[\"Ss])\\s*(?<orientation>[NS])$")
        .matcher(latitudeString);
    
    if (!matcher.matches()) {
      matcher = Pattern.compile("^(?<degrees>\\d{1,2})[°Dd]\\s*(?<minutes>\\d{1,2}.\\d{1,})['Mm]\\s*(?<orientation>[NS])$")
          .matcher(latitudeString);
      
      if (!matcher.matches()) {
        return doubleFromMap(properties, propertyName, row, runtimeException);
      } else {
        String degreesString = matcher.group("degrees");
        String minutesString = matcher.group("minutes");
        String orientationString = matcher.group("orientation");

        int degrees = Integer.parseInt(degreesString);
        double minutes = Double.parseDouble(minutesString);

        double result = Math.round((degrees + (minutes / 60)) * 1000000d) / 1000000d;
        if (orientationString.equals("S")) {
          result *= -1;
        }

        return result;
      }
    } else {
      String degreesString = matcher.group("degrees");
      String minutesString = matcher.group("minutes");
      String secondsString = matcher.group("seconds");
      String orientationString = matcher.group("orientation");

      int degrees = Integer.parseInt(degreesString.substring(0, degreesString.length() - 1));
      int minutes = Integer.parseInt(minutesString.substring(0, minutesString.length() - 1));
      int seconds = Integer.parseInt(secondsString.substring(0, secondsString.length() - 1));

      double result = Math.round((degrees + ((double) minutes / 60) + ((double) seconds / 3600)) * 1000000d) / 1000000d;
      if (orientationString.equals("S")) {
        result *= -1;
      }
      
      return result;
    }
  }

  public static Double longitudeFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {

    String latitudeString = stringFromMap(properties, propertyName);

    Matcher matcher = Pattern.compile("^(?<degrees>\\d{1,3}[°Dd])\\s*(?<minutes>\\d{1,2}['Mm])\\s*(?<seconds>\\d{1,2}[\"Ss])\\s*(?<orientation>[EW])$")
        .matcher(latitudeString);

    if (!matcher.matches()) {
      matcher = Pattern.compile("^(?<degrees>\\d{1,3})[°Dd]\\s*(?<minutes>\\d{1,2}.\\d{1,})['Mm]\\s*(?<orientation>[EW])$")
          .matcher(latitudeString);

      if (!matcher.matches()) {
        return doubleFromMap(properties, propertyName, row, runtimeException);
      } else {
        String degreesString = matcher.group("degrees");
        String minutesString = matcher.group("minutes");
        String orientationString = matcher.group("orientation");

        int degrees = Integer.parseInt(degreesString);
        double minutes = Double.parseDouble(minutesString);

        double result = Math.round((degrees + (minutes / 60)) * 1000000d) / 1000000d;
        if (orientationString.equals("W")) {
          result *= -1;
        }

        return result;
      }
    } else {
      String degreesString = matcher.group("degrees");
      String minutesString = matcher.group("minutes");
      String secondsString = matcher.group("seconds");
      String orientationString = matcher.group("orientation");

      int degrees = Integer.parseInt(degreesString.substring(0, degreesString.length() - 1));
      int minutes = Integer.parseInt(minutesString.substring(0, minutesString.length() - 1));
      int seconds = Integer.parseInt(secondsString.substring(0, secondsString.length() - 1));

      double result = Math.round((degrees + ((double) minutes / 60) + ((double) seconds / 3600)) * 1000000d) / 1000000d;
      if (orientationString.equals("W")) {
        result *= -1;
      }

      return result;
    }
  }


}
