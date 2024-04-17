package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.SoundSource;
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

final class TranslatorUtils {
  
  private enum SensorType {
    audio, depth, other
  }

  public static <O> O convertMapToObject(Map<String, Optional<String>> propertyMap, Class<O> clazz)
      throws TranslationException {
    if (clazz.isAssignableFrom(Ship.class)) {
      return (O) shipFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      return (O) seaFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Project.class)) {
      return (O) projectFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      return (O) platformFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      return (O) sensorFromMap(propertyMap);
    } else if (clazz.isAssignableFrom(SoundSource.class)) {
      return (O) soundSourceFromMap(propertyMap);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }

  public static void validateTranslator(TabularTranslator<? extends TabularTranslationField> translator, Class<?> clazz) throws TranslationException {
    Set<String> translatorFields = translator.getFields().stream()
        .map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());
    
    if (clazz.isAssignableFrom(Ship.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Ship.class);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Sea.class);
    } else if (clazz.isAssignableFrom(Project.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Project.class);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, Platform.class);
    } else if (clazz.isAssignableFrom(SoundSource.class)) {
      validateObjectWithUUIDAndUniqueFieldTranslator(translatorFields, SoundSource.class);
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      validateSensorTranslator(translatorFields);
    } else {
      throw new TranslationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }
  
  private static void validateSensorTranslator(Set<String> propertyNames) throws TranslationException {
    Set<String> baseRequiredFields = new HashSet<>(Set.of(
        "uuid", "name", "description", "position.x", "position.y", "position.z", "type"
    ));

    Set<String> missingFields = baseRequiredFields.stream()
        .filter(v -> !propertyNames.contains(v))
        .collect(Collectors.toSet());
    
    if (!missingFields.isEmpty()) {
      throw new TranslationException(String.format(
          "Translator does not fully describe %s. Missing fields: %s", Sensor.class.getSimpleName(), missingFields
      ));
    }
  }
  
  private static void validateObjectWithUUIDAndUniqueFieldTranslator(Set<String> propertyNames, Class<?> clazz) throws TranslationException {
    Set<String> missingFieldNames = Arrays.stream(clazz.getDeclaredFields())
        .map(Field::getName)
        .filter(name -> !propertyNames.contains(name))
        .collect(Collectors.toSet());

    if (!missingFieldNames.isEmpty()) {
      throw new TranslationException(
          String.format(
              "Translator does not fully describe %s. Missing fields: %s", clazz.getSimpleName(), missingFieldNames
          )
      );
    }
  }
  
  private static Sensor sensorFromMap(Map<String, Optional<String>> propertyMap)
      throws TranslationException {
    List<FormatException> exceptions = new ArrayList<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FormatException e) {
      exceptions.add(e);
    }
    
    String name = getProperty(propertyMap, "name");
    String description = getProperty(propertyMap, "description");

    Float x = null;
    try {
      x = getPropertyAsFloat(propertyMap, "position.x");
    } catch (FormatException e) {
      exceptions.add(e);
    }

    Float y = null;
    try {
      y = getPropertyAsFloat(propertyMap, "position.y");
    } catch (FormatException e) {
      exceptions.add(e);
    }

    Float z = null;
    try {
      z = getPropertyAsFloat(propertyMap, "position.z");
    } catch (FormatException e) {
      exceptions.add(e);
    }
    
    Position position = Position.builder()
        .x(x)
        .y(y)
        .z(z)
        .build();
    
    SensorType type = null;
    try {
      type = getSensorType(propertyMap);
    } catch (FormatException e) {
      exceptions.add(e);
    }
    
    Sensor sensor = null;
    if (SensorType.depth.equals(type)) {
      sensor = DepthSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .build();
    } else if (SensorType.audio.equals(type)) {
      sensor = AudioSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .hydrophoneId(getProperty(propertyMap, "hydrophoneId"))
          .preampId(getProperty(propertyMap, "preampId"))
          .build();
    } else if (SensorType.other.equals(type)) {
      sensor = OtherSensor.builder()
          .uuid(uuid)
          .name(name)
          .position(position)
          .description(description)
          .properties(getProperty(propertyMap, "properties"))
          .sensorType(getProperty(propertyMap, "sensorType"))
          .build();
    }
    
    if (!exceptions.isEmpty()) {
      throw new TranslationException("Translation failed", exceptions);
    }
    
    return sensor;
  }

  private static SoundSource soundSourceFromMap(Map<String, Optional<String>> propertyMap) throws TranslationException {

    List<FormatException> exceptions = new ArrayList<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FormatException e) {
      exceptions.add(e);
    }

    if (!exceptions.isEmpty()) {
      throw new TranslationException("Translation failed", exceptions);
    }
    
    return SoundSource.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .scientificName(getProperty(propertyMap, "scientificName"))
        .build();
  }
  
  private static Ship shipFromMap(Map<String, Optional<String>> propertyMap) throws TranslationException {
    
    List<FormatException> exceptions = new ArrayList<>(0);
    
    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FormatException e) {
      exceptions.add(e);
    }

    if (!exceptions.isEmpty()) {
      throw new TranslationException("Translation failed", exceptions);
    }
    
    return Ship.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }

  private static Sea seaFromMap(Map<String, Optional<String>> propertyMap) throws TranslationException {

    List<FormatException> violations = new ArrayList<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FormatException e) {
      violations.add(e);
    }

    if (!violations.isEmpty()) {
      throw new TranslationException("Translation failed", violations);
    }

    return Sea.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }

  private static Project projectFromMap(Map<String, Optional<String>> propertyMap) throws TranslationException {

    List<FormatException> violations = new ArrayList<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FormatException e) {
      violations.add(e);
    }
    
    if (!violations.isEmpty()) {
      throw new TranslationException("Translation failed", violations);
    }

    return Project.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }

  private static Platform platformFromMap(Map<String, Optional<String>> propertyMap) throws TranslationException {

    List<FormatException> violations = new ArrayList<>(0);

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FormatException e) {
      violations.add(e);
    }

    if (!violations.isEmpty()) {
      throw new TranslationException("Translation failed", violations);
    }

    return Platform.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }
  
  private static UUID uuidFromString(String uuidString) throws FormatException {
    if (uuidString == null || StringUtils.isBlank(uuidString)) {
      return null;
    }
    
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new FormatException("uuid", "invalid uuid format", e);
    }
  }
  
  private static SensorType getSensorType(Map<String, Optional<String>> map) throws FormatException {
    String typeString = getProperty(map, "type");
    if (typeString == null || StringUtils.isBlank(typeString)) {
      return null;
    }
    
    try {
      return SensorType.valueOf(typeString);
    } catch (Exception e) {
      throw new FormatException("type", String.format(
          "Invalid sensor type. Was not one of %s", Arrays.stream(SensorType.values())
              .map(Enum::name)
              .collect(Collectors.joining(", "))
      ), e);
    }
  }
  
  private static String getProperty(Map<String, Optional<String>> map, String property) {
    Optional<String> value = map.get(property);
    if (value == null) {
      return null;
    }
    
    return value.orElse(null);
  }
  
  private static Float getPropertyAsFloat(Map<String, Optional<String>> map, String property) throws FormatException {
    String propertyStringValue = getProperty(map, property);
    
    if (propertyStringValue == null) {
      return null;
    }
    
    try {
      return Float.parseFloat(propertyStringValue);
    } catch (NumberFormatException e) {
      throw new FormatException(property, "invalid number format", e);
    }
  }
}
