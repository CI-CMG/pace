package edu.colorado.cires.pace.translator.converter;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateOnlyTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTimeSeparatedTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DateTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.DefaultTimeTranslator;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.jshell.EvalException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ConversionUtils provides functions for converting between
 * common data types used in PACE
 */
final class ConversionUtils {
  
  private static <T> T transformedPropertyFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException, Function<String, T> transform, String errorMessage) {
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, propertyName);
    String property = stringFromProperty(valueWithColumnNumber);
    
    if (property == null) {
      return null;
    }
    
    try {
      return transform.apply(property);
    } catch (Throwable e) {
      runtimeException.addSuppressed(new FieldException(
          propertyName, targetProperty, errorMessage, valueWithColumnNumber.column(), row
      ));
      return null;
    }
  }

  /**
   * Returns the relevant uuid given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param propertyName property name to search for
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return UUID related to provided property name
   */
  public static UUID uuidFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, targetProperty, propertyName, row, runtimeException, UUID::fromString, "Invalid UUID format");
  }

  /**
   * Returns the relevant float given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param propertyName property name to search for
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return Float related to provided property name
   */
  public static Float floatFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, targetProperty, propertyName, row, runtimeException, Float::parseFloat, "Invalid float format");
  }

  /**
   * Returns the relevant double given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param propertyName property name to search for
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return Double related to provided property name
   */
  public static Double doubleFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, targetProperty, propertyName, row, runtimeException, Double::parseDouble, "Invalid double format");
  }

  /**
   * Returns the relevant integer given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param propertyName property name to search for
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return Integer related to provided property name
   */
  public static Integer integerFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, targetProperty, propertyName, row, runtimeException, Integer::parseInt, "Invalid integer format");
  }

  /**
   * Returns the relevant path given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param propertyName property name to search for
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return Path related to provided property name
   */
  public static Path pathFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException) {
    return transformedPropertyFromMap(properties, targetProperty, propertyName, row, runtimeException, Paths::get, "Invalid path format");
  }

  /**
   * Returns the relevant local date given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param dateTranslator relevant date translator
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return LocalDate related to provided property name
   */
  public static LocalDate localDateFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, DateTranslator dateTranslator, int row, RuntimeException runtimeException) {
    return parseLocalDate(properties, targetProperty, dateTranslator, row, runtimeException);
  }
  
  private static LocalDate parseLocalDate(Map<String, ValueWithColumnNumber> properties, String targetProperty, DateTranslator dateTranslator, int row, RuntimeException runtimeException) {
    if (dateTranslator == null) {
      return null;
    }
    
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
          dateTranslator.getTimeZone(), "Time Zone", "Invalid time zone", timeZone.column(), row
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
          dateTranslator.getDate(), targetProperty, "Invalid date format", date.column(), row 
      ));
      return null;
    }
  }

  /**
   * Returns the relevant local date time given the provided property name
   * @param properties maps strings to relevant property values
   * @param targetProperty name of property that is being searched for
   * @param timeTranslator relevant time translator
   * @param row the relevant row to include in error outputs
   * @param runtimeException thrown in case of error while mapping
   * @return LocalDateTime related to provided property name
   */
  public static LocalDateTime localDateTimeFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, TimeTranslator timeTranslator, int row, RuntimeException runtimeException) {
    if (timeTranslator == null) {
      return null;
    }
    
    ValueWithColumnNumber valueWithColumnNumber = propertyFromMap(properties, timeTranslator.getTimeZone());
    String timeZone = valueWithColumnNumber.value().orElse(null);
    if (timeZone == null) {
      return null;
    }
    if (timeTranslator instanceof DateOnlyTimeTranslator) {
      timeTranslator = DefaultTimeTranslator.builder()
          .time(((DateOnlyTimeTranslator) timeTranslator).getDate()+"T00:00:00")
          .timeZone("timeZone")
          .build();
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
            timeTranslator.getTimeZone(), "Time Zone", "Invalid time zone", valueWithColumnNumber.column(), row
        ));
        return null;
      }
      return transformedPropertyFromMap(properties, targetProperty, defaultTimeTranslator.getTime(), row, runtimeException, (s) -> parseLocalDateTime(s, dateTimeFormatter), "Invalid date time format");
    } else if (timeTranslator instanceof DateTimeSeparatedTimeTranslator dateTimeSeparatedTimeTranslator) {
      return localDateTimeFromMap(properties, targetProperty, dateTimeSeparatedTimeTranslator, row, runtimeException);
    } else if (timeTranslator instanceof DateOnlyTimeTranslator dateOnlyTimeTranslator) {
      return localDateTimeFromMap(properties, targetProperty, dateOnlyTimeTranslator, row, runtimeException);
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

  private static LocalDateTime localDateTimeFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, DateTimeSeparatedTimeTranslator timeTranslator, int row, RuntimeException runtimeException) {
    String zoneId = stringFromMap(properties, timeTranslator.getTimeZone());
    
    DateTranslator dateTranslator = DateTranslator.builder()
        .date(timeTranslator.getDate())
        .timeZone(timeTranslator.getTimeZone())
        .build();
    
    LocalDate date = parseLocalDate(
        properties,
        targetProperty,
        dateTranslator,
        row,
        runtimeException
    );
    if (date == null) {
      return null;
    }
    LocalTime time = transformedPropertyFromMap(properties, targetProperty, timeTranslator.getTime(), row, runtimeException, (s) -> parseLocalTime(s, zoneId), "Invalid time format");
    if (time == null) {
      return date.atTime(0, 0);
    }
    return date.atTime(time);
  }

  /**
   * Pulls the property value from the map related to the property name provided
   * @param properties maps property names to values
   * @param propertyName property name to search by
   * @return ValueWithColumnNumber of property name's mapping in properties
   */
  public static @NotNull ValueWithColumnNumber propertyFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    ValueWithColumnNumber valueWithColumnNumber = null;
    if (propertyName != null) {
      valueWithColumnNumber = properties.get(propertyName); 
    }
    
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

  /**
   * Parses semicolon delimited list of property names and returns the relevant
   * mappings in properties for each object in the list
   * @param properties maps property names to values
   * @param propertyName property names to search by, separated by semicolons
   * @return List of string property values
   */
  public static List<String> stringListFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    String value = stringFromMap(properties, propertyName);
    if (value == null) {
      return Collections.emptyList();
    }
    return Arrays.stream(value.split(";")).toList();
  }

  /**
   * Parses semicolon delimited list of property names and returns the relevant
   * mappings in properties for each object in the list
   * @param properties maps property names to values
   * @param propertyName property names to search by, separated by semicolons
   * @return List of string property values
   */
  public static List<String> stringListFromMapReplace(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    String value = stringFromMap(properties, propertyName);
    if (value == null) {
      return Collections.emptyList();
    }
    return Arrays.stream(
        value.split(";"))
        .map(s -> s.replace(" ", "_"))
        .toList();
  }

  /**
   * Pulls the relevant property value string using property name to identify value
   * @param properties maps property names to values
   * @param propertyName property name to search by
   * @return String property value related to property name
   */
  public static @Nullable String stringFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    return stringFromProperty(
        propertyFromMap(properties, propertyName)
    );
  }

  /**
   * Pulls the relevant property value string using property name to identify value
   * @param properties maps property names to values
   * @param propertyName property name to search by
   * @return String property value related to property name
   */
  public static @Nullable String stringFromMapReplace(Map<String, ValueWithColumnNumber> properties, String propertyName) {
    String s = stringFromProperty(
        propertyFromMap(properties, propertyName)
    );
    if (s != null) {
      s = s.replace(" ", "_");
    }
    return s;
  }

  /**
   * Pulls string value mapping given value with column number
   * @param value value to map to String
   * @return String version of value
   */
  public static String stringFromProperty(ValueWithColumnNumber value) {
    return value.value()
        .map(string -> StringUtils.isBlank(string) ? null : string.trim())
        .orElse(null);
  }

  /**
   * Returns list of paths from properties as queried by property name
   * @param properties maps property name to value
   * @param targetProperty name of property being searched for
   * @param propertyName property name to search by
   * @param row number to be included in error output
   * @param runtimeException thrown in case of error mapping
   * @return List of paths as indicated by property name
   */
  public static List<Path> pathListFromMap(Map<String, ValueWithColumnNumber> properties, String targetProperty, String propertyName, int row, RuntimeException runtimeException) {
    if (propertyName == null) {
      return Collections.emptyList();
    }
    ValueWithColumnNumber valueWithColumnNumber = properties.get(propertyName);
    if (valueWithColumnNumber == null) {
      return Collections.emptyList();
    }
    String string = stringFromProperty(valueWithColumnNumber);
    if (string == null) {
      return Collections.emptyList();
    }
    
    return Arrays.stream(string.split(";"))
            .map(value -> pathFromMap(
                Map.of(propertyName, new ValueWithColumnNumber(Optional.of(value), valueWithColumnNumber.column())),
                targetProperty,
                propertyName,
                row,
                runtimeException
            ))
            .toList();
  }

  /**
   * Pulls latitude from properties as queried by property name
   * @param properties maps property name to value
   * @param propertyName property name to query by
   * @param row number to be included in error output
   * @param runtimeException thrown in case of error mapping
   * @return Double latitude
   */
  public static Double latitudeFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {
    
    String latitudeString = stringFromMap(properties, propertyName);
    
    if (StringUtils.isBlank(latitudeString)) {
      return null;
    }

    Matcher matcher = Pattern.compile("^(?<degrees>\\d{1,2}[°Dd])\\s*(?<minutes>\\d{1,2}['Mm])\\s*(?<seconds>\\d{1,2}[\"Ss])\\s*(?<orientation>[NS])$")
        .matcher(latitudeString);
    
    if (!matcher.matches()) {
      matcher = Pattern.compile("^(?<degrees>\\d{1,2})[°Dd]\\s*(?<minutes>\\d{1,2}.\\d{1,})['Mm]\\s*(?<orientation>[NS])$")
          .matcher(latitudeString);
      
      if (!matcher.matches()) {
        return doubleFromMap(properties, "Latitude", propertyName, row, runtimeException);
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

  /**
   * Pulls longitude from properties as queried by property name
   * @param properties maps property name to value
   * @param propertyName property name to query by
   * @param row number to be included in error output
   * @param runtimeException thrown in case of error mapping
   * @return Double longitude
   */
  public static Double longitudeFromMap(Map<String, ValueWithColumnNumber> properties, String propertyName, int row, RuntimeException runtimeException) {

    String latitudeString = stringFromMap(properties, propertyName);
    
    if (StringUtils.isBlank(latitudeString)) {
      return null;
    }

    Matcher matcher = Pattern.compile("^(?<degrees>\\d{1,3}[°Dd])\\s*(?<minutes>\\d{1,2}['Mm])\\s*(?<seconds>\\d{1,2}[\"Ss])\\s*(?<orientation>[EW])$")
        .matcher(latitudeString);

    if (!matcher.matches()) {
      matcher = Pattern.compile("^(?<degrees>\\d{1,3})[°Dd]\\s*(?<minutes>\\d{1,2}.\\d{1,})['Mm]\\s*(?<orientation>[EW])$")
          .matcher(latitudeString);

      if (!matcher.matches()) {
        return doubleFromMap(properties, "Longitude", propertyName, row, runtimeException);
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
