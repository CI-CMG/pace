package edu.colorado.cires.pace.translator;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
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
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

final class TranslatorUtils {

  public static <O> O convertMapToObject(Map<String, Optional<String>> propertyMap, Class<O> clazz, int row, CRUDRepository<?>... dependencyRepositories)
      throws RowConversionException {
    if (clazz.isAssignableFrom(Ship.class)) {
      return (O) shipFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      return (O) seaFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(Project.class)) {
      return (O) projectFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      return (O) platformFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      return (O) sensorFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(SoundSource.class)) {
      return (O) soundSourceFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(FileType.class)) {
      return (O) fileTypeFromMap(propertyMap, row);
    } else if (clazz.isAssignableFrom(Instrument.class)) {
      CRUDRepository<?> repository = Arrays.stream(dependencyRepositories)
          .filter(r -> r instanceof FileTypeRepository)
          .findFirst().orElseThrow(
              () -> new RowConversionException("Instrument translation missing fileType repository", row)
          );
      return (O) instrumentFromMap(propertyMap, (FileTypeRepository) repository, row);
    } else {
      throw new RowConversionException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ), row);
    }
  }

  public static void validateTranslator(TabularTranslator<? extends TabularTranslationField> translator, Class<?> clazz) throws TranslatorValidationException {
    Set<String> translatorFields = translator.getFields().stream()
        .map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());
    
    if (clazz.isAssignableFrom(Ship.class)) {
      validateTranslatorWithFlatFields(translatorFields, Ship.class);
    } else if (clazz.isAssignableFrom(Sea.class)) {
      validateTranslatorWithFlatFields(translatorFields, Sea.class);
    } else if (clazz.isAssignableFrom(Project.class)) {
      validateTranslatorWithFlatFields(translatorFields, Project.class);
    } else if (clazz.isAssignableFrom(Platform.class)) {
      validateTranslatorWithFlatFields(translatorFields, Platform.class);
    } else if (clazz.isAssignableFrom(SoundSource.class)) {
      validateTranslatorWithFlatFields(translatorFields, SoundSource.class);
    } else if (clazz.isAssignableFrom(Sensor.class)) {
      validateSensorTranslator(translatorFields);
    } else if (clazz.isAssignableFrom(Instrument.class)) {
      validateInstrumentTranslator(translator);
    } else if (clazz.isAssignableFrom(FileType.class)) {
      validateTranslatorWithFlatFields(translatorFields, FileType.class);
    } else {
      throw new TranslatorValidationException(String.format(
          "Translation not supported for %s", clazz.getSimpleName()
      ));
    }
  }
  
  private static void validateInstrumentTranslator(TabularTranslator<? extends TabularTranslationField> translator) throws TranslatorValidationException {
    Set<String> baseRequiredFields = new HashSet<>(Set.of(
        "uuid", "name", "fileTypes" 
    ));
    
    Set<String> fieldNames = translator.getFields().stream()
        .map(TabularTranslationField::getPropertyName)
        .collect(Collectors.toSet());

    Set<String> missingFields = baseRequiredFields.stream()
        .filter(v -> !fieldNames.contains(v))
        .collect(Collectors.toSet());

    if (!missingFields.isEmpty()) {
      throw new TranslatorValidationException(String.format(
          "Translator does not fully describe %s. Missing fields: %s", Instrument.class.getSimpleName(), missingFields
      ));
    }
  }
  
  private static void validateSensorTranslator(Set<String> propertyNames) throws TranslatorValidationException {
    Set<String> baseRequiredFields = new HashSet<>(Set.of(
        "uuid", "name", "description", "position.x", "position.y", "position.z", "type"
    ));

    Set<String> missingFields = baseRequiredFields.stream()
        .filter(v -> !propertyNames.contains(v))
        .collect(Collectors.toSet());
    
    if (!missingFields.isEmpty()) {
      throw new TranslatorValidationException(String.format(
          "Translator does not fully describe %s. Missing fields: %s", Sensor.class.getSimpleName(), missingFields
      ));
    }
  }
  
  private static void validateTranslatorWithFlatFields(Set<String> propertyNames, Class<?> clazz) throws TranslatorValidationException {
    Set<String> missingFieldNames = Arrays.stream(clazz.getDeclaredFields())
        .map(Field::getName)
        .filter(name -> !propertyNames.contains(name))
        .collect(Collectors.toSet());

    if (!missingFieldNames.isEmpty()) {
      throw new TranslatorValidationException(
          String.format(
              "Translator does not fully describe %s. Missing fields: %s", clazz.getSimpleName(), missingFieldNames
          )
      );
    }
  }
  
  private static FileType fileTypeFromMap(Map<String, Optional<String>> propertyMap, int row) throws RowConversionException {
    RuntimeException runtimeException = new RuntimeException();
    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    String type = getProperty(propertyMap, "type");
    String comment = getProperty(propertyMap, "comment");
    
    if (runtimeException.getSuppressed().length > 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }
    
    return FileType.builder()
        .uuid(uuid)
        .type(type)
        .comment(comment)
        .build();
  }
  
  private static Instrument instrumentFromMap(Map<String, Optional<String>> propertyMap, FileTypeRepository fileTypeRepository, int row)
      throws RowConversionException {
    RuntimeException runtimeException = new RuntimeException();
    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    String name = getProperty(propertyMap, "name");

    String fileTypeNames = getProperty(propertyMap, "fileTypes");
    
    List<FileType> fileTypes;
    if (fileTypeNames != null) {
      fileTypes = Arrays.stream(fileTypeNames.split(";"))
        .map(ft -> {
        try {
          return fileTypeRepository.getByUniqueField(ft);
        } catch (DatastoreException | NotFoundException e) {
          runtimeException.addSuppressed(new FieldException(
              "fileTypes", e.getMessage()
          ));
          return null;
        }
      }).toList();
    } else {
      fileTypes = Collections.emptyList();
    }
    
    if (runtimeException.getSuppressed().length > 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }
    
    return Instrument.builder()
        .uuid(uuid)
        .name(name)
        .fileTypes(fileTypes)
        .build();
  }
  
  private static Sensor sensorFromMap(Map<String, Optional<String>> propertyMap, int row)
      throws RowConversionException {
    RuntimeException runtimeException = new RuntimeException();

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }
    
    String name = getProperty(propertyMap, "name");
    String description = getProperty(propertyMap, "description");

    Float x = null;
    try {
      x = getPropertyAsFloat(propertyMap, "position.x", row);
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    Float y = null;
    try {
      y = getPropertyAsFloat(propertyMap, "position.y", row);
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    Float z = null;
    try {
      z = getPropertyAsFloat(propertyMap, "position.z", row);
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }
    
    Position position = Position.builder()
        .x(x)
        .y(y)
        .z(z)
        .build();
    
    SensorType type = null;
    try {
      type = getSensorType(propertyMap, row);
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
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
    
    if (runtimeException.getSuppressed().length != 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }
    
    return sensor;
  }

  private static SoundSource soundSourceFromMap(Map<String, Optional<String>> propertyMap, int row) throws RowConversionException {

    RuntimeException runtimeException = new RuntimeException();

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    if (runtimeException.getSuppressed().length != 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }
    
    return SoundSource.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .scientificName(getProperty(propertyMap, "scientificName"))
        .build();
  }
  
  private static Ship shipFromMap(Map<String, Optional<String>> propertyMap, int row) throws RowConversionException {
    
    RuntimeException runtimeException = new RuntimeException();
    
    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    if (runtimeException.getSuppressed().length != 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }
    
    return Ship.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }

  private static Sea seaFromMap(Map<String, Optional<String>> propertyMap, int row) throws RowConversionException {

    RuntimeException runtimeException = new RuntimeException();

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    if (runtimeException.getSuppressed().length != 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }

    return Sea.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }

  private static Project projectFromMap(Map<String, Optional<String>> propertyMap, int row) throws RowConversionException {

    RuntimeException runtimeException = new RuntimeException();

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }
    
    if (runtimeException.getSuppressed().length != 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }

    return Project.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }

  private static Platform platformFromMap(Map<String, Optional<String>> propertyMap, int row) throws RowConversionException {

    RuntimeException runtimeException = new RuntimeException();

    UUID uuid = null;
    try {
      uuid = uuidFromString(getProperty(propertyMap, "uuid"));
    } catch (FieldException e) {
      runtimeException.addSuppressed(e);
    }

    if (runtimeException.getSuppressed().length != 0) {
      RowConversionException exception = new RowConversionException("Translation failed", row);
      for (Throwable throwable : runtimeException.getSuppressed()) {
        exception.addSuppressed(throwable);
      }
      throw exception;
    }

    return Platform.builder()
        .uuid(uuid)
        .name(getProperty(propertyMap, "name"))
        .build();
  }
  
  private static UUID uuidFromString(String uuidString) throws FieldException {
    if (uuidString == null || StringUtils.isBlank(uuidString)) {
      return null;
    }
    
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new FieldException("uuid", "invalid uuid format");
    }
  }
  
  private static SensorType getSensorType(Map<String, Optional<String>> map, int row) throws FieldException {
    String typeString = getProperty(map, "type");
    if (typeString == null || StringUtils.isBlank(typeString)) {
      return null;
    }
    
    try {
      return SensorType.valueOf(typeString);
    } catch (Exception e) {
      throw new FieldException("type", String.format(
          "Invalid sensor type. Was not one of %s", Arrays.stream(SensorType.values())
              .map(Enum::name)
              .collect(Collectors.joining(", "))
      ));
    }
  }
  
  private static String getProperty(Map<String, Optional<String>> map, String property) {
    Optional<String> value = map.get(property);
    if (value == null) {
      return null;
    }
    
    return value.orElse(null);
  }
  
  private static Float getPropertyAsFloat(Map<String, Optional<String>> map, String property, int row) throws FieldException {
    String propertyStringValue = getProperty(map, property);
    
    if (propertyStringValue == null) {
      return null;
    }
    
    try {
      return Float.parseFloat(propertyStringValue);
    } catch (NumberFormatException e) {
      throw new FieldException(property, "invalid number format");
    }
  }
}
