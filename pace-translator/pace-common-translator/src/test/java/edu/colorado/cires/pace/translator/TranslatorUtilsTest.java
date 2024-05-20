package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.SoundPropagationModelsPackage;
import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.CPODPackage;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.LocationDetail;
import edu.colorado.cires.pace.data.object.MobileMarineLocation;
import edu.colorado.cires.pace.data.object.MultiPointStationaryMarineLocation;
import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.SoundClipsPackage;
import edu.colorado.cires.pace.data.object.SoundLevelMetricsPackage;
import edu.colorado.cires.pace.data.object.StationaryMarineLocation;
import edu.colorado.cires.pace.data.object.StationaryTerrestrialLocation;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.InstrumentRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.repository.PlatformRepository;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.repository.SensorRepository;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class TranslatorUtilsTest {
  
  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class,
      DetectionType.class,
      FileType.class,
      Person.class,
      Organization.class,
      Instrument.class,
      
  })
  void testValidateTranslators(Class<?> clazz) {
    List<String> expectedFieldNames = FieldNameFactory.getDefaultDeclaredFields(clazz);
    Map<String, ValueWithColumnNumber> propertyMap = IntStream.range(0, expectedFieldNames.size()).boxed()
        .collect(Collectors.toMap(
            expectedFieldNames::get,
            i -> new ValueWithColumnNumber(
                Optional.of(""),
                i
            )
        ));

    CRUDRepository<?>[] dependencyRepositories;
    if (clazz.getSimpleName().equals("Instrument")) {
      dependencyRepositories = new CRUDRepository[]{mock(FileTypeRepository.class)};
    } else {
      dependencyRepositories = new CRUDRepository[0];
    }
    
    assertDoesNotThrow(() -> TranslatorUtils.convertMapToObject(
        propertyMap, clazz, 0, dependencyRepositories
    ));

    String removedFieldName = expectedFieldNames.get(expectedFieldNames.size() - 1);
    propertyMap.remove(removedFieldName);
    
    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, clazz, 0, dependencyRepositories
    ));
    
    assertEquals("Translation failed", exception.getMessage());
    
    assertEquals(1, exception.getSuppressed().length);
    Throwable suppressed = exception.getSuppressed()[0];
    assertInstanceOf(TranslatorValidationException.class, suppressed);
    TranslatorValidationException translatorValidationException = (TranslatorValidationException) suppressed;
    assertEquals(String.format("Translator missing required field '%s'", removedFieldName), translatorValidationException.getMessage());
    
  }
  
  @ParameterizedTest
  @EnumSource(SensorType.class)
  void testValidateSensorTranslator(SensorType sensorType) {
    List<String> expectedFieldNames = FieldNameFactory.getSensorDeclaredFields(sensorType);
    Map<String, ValueWithColumnNumber> propertyMap = IntStream.range(0, expectedFieldNames.size()).boxed().collect(Collectors.toMap(
        expectedFieldNames::get,
        i -> {
          Optional<String> value = expectedFieldNames.get(i).equals("type") ? Optional.of(sensorType.name()) : Optional.of("");
          return new ValueWithColumnNumber(value, i);
        }
    ));


    assertDoesNotThrow(() -> TranslatorUtils.convertMapToObject(
        propertyMap, Sensor.class, 0
    ));

    String removedFieldName = expectedFieldNames.get(expectedFieldNames.size() - 1);
    propertyMap.remove(removedFieldName);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, Sensor.class, 0
    ));

    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    Throwable suppressed = exception.getSuppressed()[0];
    assertInstanceOf(TranslatorValidationException.class, suppressed);
    TranslatorValidationException translatorValidationException = (TranslatorValidationException) suppressed;
    assertEquals(String.format("Translator missing required field '%s'", removedFieldName), translatorValidationException.getMessage());

    propertyMap.put("type", new ValueWithColumnNumber(
        Optional.of("test-type"),
        propertyMap.get("type").column()
    ));
    exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, Sensor.class, 0
    ));

    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    suppressed = exception.getSuppressed()[0];
    assertInstanceOf(FieldException.class, suppressed);
    FieldException fieldException = (FieldException) suppressed;
    assertEquals("Invalid sensor type. Was not one of audio, depth, other", fieldException.getMessage());

    propertyMap.remove("type");
    exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, Sensor.class, 0
    ));

    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    suppressed = exception.getSuppressed()[0];
    assertInstanceOf(TranslatorValidationException.class, suppressed);
    translatorValidationException = (TranslatorValidationException) suppressed;
    assertEquals("Translator missing required field 'type'", translatorValidationException.getMessage());
  }
  
  @ParameterizedTest
  @CsvSource(value = {
      "SOUND_CLIPS,STATIONARY_MARINE",
      "AUDIO,STATIONARY_MARINE",
      "CPOD,STATIONARY_MARINE",
      "DETECTIONS,STATIONARY_MARINE",
      "SOUND_LEVEL_METRICS,STATIONARY_MARINE",
      "SOUND_PROPAGATION_MODELS,STATIONARY_MARINE",
      "SOUND_PROPAGATION_MODELS,MULTIPOINT_STATIONARY_MARINE",
      "SOUND_PROPAGATION_MODELS,MOBILE_MARINE",
      "SOUND_PROPAGATION_MODELS,STATIONARY_TERRESTRIAL",
  })
  void testValidateDatasetTranslator(DatasetType datasetType, LocationType locationType) {
    List<String> expectedFieldNames = FieldNameFactory.getDatasetDeclaredFields(datasetType, locationType);
    Map<String, ValueWithColumnNumber> propertyMap = IntStream.range(0, expectedFieldNames.size()).boxed().collect(Collectors.toMap(
        expectedFieldNames::get,
        i -> {
          Optional<String> value;
          if (expectedFieldNames.get(i).equals("datasetType")) {
            value = Optional.of(datasetType.getName());
          } else if (expectedFieldNames.get(i).equals("locationType")) {
            value = Optional.of(locationType.getName());
          } else {
            value = Optional.of("");
          }
          
          return new ValueWithColumnNumber(value, i);
        }
    ));
    
    CRUDRepository<?>[] dependencyRepositories = new CRUDRepository[] {
        mock(ProjectRepository.class),
        mock(PersonRepository.class),
        mock(OrganizationRepository.class),
        mock(PlatformRepository.class),
        mock(InstrumentRepository.class),
        mock(SensorRepository.class),
        mock(DetectionTypeRepository.class),
        mock(SeaRepository.class),
        mock(ShipRepository.class)
    };


    assertDoesNotThrow(() -> TranslatorUtils.convertMapToObject(
        propertyMap, Package.class, 0, dependencyRepositories
    ));

    String removedFieldName = expectedFieldNames.get(expectedFieldNames.size() - 1);
    propertyMap.remove(removedFieldName);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, Package.class, 0, dependencyRepositories
    ));

    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    Throwable suppressed = exception.getSuppressed()[0];
    assertInstanceOf(TranslatorValidationException.class, suppressed);
    TranslatorValidationException translatorValidationException = (TranslatorValidationException) suppressed;
    assertEquals(String.format("Translator missing required field '%s'", removedFieldName), translatorValidationException.getMessage());

    propertyMap.put("datasetType", new ValueWithColumnNumber(
        Optional.of("test-datasetType"),
        propertyMap.get("datasetType").column()
    ));
    propertyMap.put("locationType", new ValueWithColumnNumber(
        Optional.of("test-locationType"),
        propertyMap.get("locationType").column()
    ));
    exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, Package.class, 0, dependencyRepositories
    ));
    assertEquals(2, exception.getSuppressed().length);
    suppressed = exception.getSuppressed()[0];
    assertInstanceOf(FieldException.class, suppressed);
    FieldException fieldException = (FieldException) suppressed;
    assertEquals("Invalid dataset type. Was not one of sound clips, audio, CPOD, detections, sound level metrics, sound propagation models", fieldException.getMessage());
    suppressed = exception.getSuppressed()[1];
    assertInstanceOf(FieldException.class, suppressed);
    fieldException = (FieldException) suppressed;
    assertEquals("Invalid location type. Was not one of stationary marine, multipoint stationary marine, mobile marine, stationary terrestrial", fieldException.getMessage());

    propertyMap.remove("datasetType");
    propertyMap.remove("locationType");
    exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(
        propertyMap, Package.class, 0, dependencyRepositories
    ));
    assertEquals(2, exception.getSuppressed().length);
    suppressed = exception.getSuppressed()[0];
    assertInstanceOf(TranslatorValidationException.class, suppressed);
    translatorValidationException = (TranslatorValidationException) suppressed;
    assertEquals("Translator missing required field 'datasetType'", translatorValidationException.getMessage());
    suppressed = exception.getSuppressed()[1];
    assertInstanceOf(TranslatorValidationException.class, suppressed);
    translatorValidationException = (TranslatorValidationException) suppressed;
    assertEquals("Translator missing required field 'locationType'", translatorValidationException.getMessage());
    
  }
  
  @Test
  void testConvertPerson() throws RowConversionException {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0));
    map.put("name", new ValueWithColumnNumber(Optional.of("person-name"), 1));
    map.put("organization", new ValueWithColumnNumber(Optional.of("person-organization"), 2));
    map.put("position", new ValueWithColumnNumber(Optional.of("person-position"), 3));
    map.put("street", new ValueWithColumnNumber(Optional.of("person-street"), 4));
    map.put("city", new ValueWithColumnNumber(Optional.of("person-city"), 5));
    map.put("state", new ValueWithColumnNumber(Optional.of("person-state"), 6));
    map.put("zip", new ValueWithColumnNumber(Optional.of("person-zip"), 7));
    map.put("country", new ValueWithColumnNumber(Optional.of("person-country"), 8));
    map.put("email", new ValueWithColumnNumber(Optional.of("person-email"), 9));
    map.put("phone", new ValueWithColumnNumber(Optional.of("person-phone"), 10));
    map.put("orcid", new ValueWithColumnNumber(Optional.of("person-orcid"), 11));
    
    Person person = TranslatorUtils.convertMapToObject(map, Person.class, 0);
    assertEquals(map.get("uuid").value().orElseThrow(), person.getUuid().toString());
    assertEquals(map.get("name").value().orElseThrow(), person.getName());
    assertEquals(map.get("organization").value().orElseThrow(), person.getOrganization());
    assertEquals(map.get("position").value().orElseThrow(), person.getPosition());
    assertEquals(map.get("street").value().orElseThrow(), person.getStreet());
    assertEquals(map.get("city").value().orElseThrow(), person.getCity());
    assertEquals(map.get("state").value().orElseThrow(), person.getState());
    assertEquals(map.get("zip").value().orElseThrow(), person.getZip());
    assertEquals(map.get("country").value().orElseThrow(), person.getCountry());
    assertEquals(map.get("email").value().orElseThrow(), person.getEmail());
    assertEquals(map.get("phone").value().orElseThrow(), person.getPhone());
    assertEquals(map.get("orcid").value().orElseThrow(), person.getOrcid());
  }
  
  @Test
  void testConvertOrganization() throws RowConversionException {
    Map<String, ValueWithColumnNumber> map = new HashMap<>(0);
    map.put("uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0));
    map.put("name", new ValueWithColumnNumber(Optional.of("organization-name"), 1));
    map.put("street", new ValueWithColumnNumber(Optional.of("organization-street"), 2));
    map.put("city", new ValueWithColumnNumber(Optional.of("organization-city"), 3));
    map.put("state", new ValueWithColumnNumber(Optional.of("organization-state"), 4));
    map.put("zip", new ValueWithColumnNumber(Optional.of("organization-zip"), 5));
    map.put("country", new ValueWithColumnNumber(Optional.of("organization-country"), 6));
    map.put("email", new ValueWithColumnNumber(Optional.of("organization-email"), 7));
    map.put("phone", new ValueWithColumnNumber(Optional.of("organization-phone"), 8));

    Organization organization = TranslatorUtils.convertMapToObject(map, Organization.class, 0);
    assertEquals(map.get("uuid").value().orElseThrow(), organization.getUuid().toString());
    assertEquals(map.get("name").value().orElseThrow(), organization.getName());
    assertEquals(map.get("street").value().orElseThrow(), organization.getStreet());
    assertEquals(map.get("city").value().orElseThrow(), organization.getCity());
    assertEquals(map.get("state").value().orElseThrow(), organization.getState());
    assertEquals(map.get("zip").value().orElseThrow(), organization.getZip());
    assertEquals(map.get("country").value().orElseThrow(), organization.getCountry());
    assertEquals(map.get("email").value().orElseThrow(), organization.getEmail());
    assertEquals(map.get("phone").value().orElseThrow(), organization.getPhone());
  }
  
  @Test
  void testUnsupportedType() {
    record TestObject(String name) {}
    
    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "p1", new ValueWithColumnNumber(Optional.of("2"), 1)
    ), TestObject.class, 1));
    assertEquals(String.format(
        "Translation not supported for %s", TestObject.class.getSimpleName()
    ), exception.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvert(Class<? extends ObjectWithName> clazz) throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1)
    );
    
    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz, 1);
    assertEquals(propertyMap.get("uuid").value().orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("name").value().orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }
  
  @Test
  void testConvertFileType() throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
        "type", new ValueWithColumnNumber(Optional.of("test-type"), 1),
        "comment", new ValueWithColumnNumber(Optional.of("test-comment"), 2)
    );

    FileType object = TranslatorUtils.convertMapToObject(propertyMap, FileType.class, 1);
    assertEquals(propertyMap.get("uuid").value().orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("type").value().orElseThrow(
        () -> new IllegalStateException("type not found in propertyMap")
    ), object.getType());
    assertEquals(propertyMap.get("comment").value().orElseThrow(
        () -> new IllegalStateException("comment not found in propertyMap")
    ), object.getComment());
  }

  @Test
  void testConvertFileTypeBadUUID() {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("TEST"), 0),
        "type", new ValueWithColumnNumber(Optional.of("test-type"), 1),
        "comment", new ValueWithColumnNumber(Optional.of("test-comment"), 2)
    );

    Exception exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, FileType.class, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> (v).getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

  @Test
  void testConvertSoundSource() throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
        "source", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "scienceName", new ValueWithColumnNumber(Optional.of("test-scientific-name"), 2)
    );

    DetectionType object = TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1);
    assertEquals(propertyMap.get("uuid").value().orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("source").value().orElseThrow(
        () -> new IllegalStateException("source not found in propertyMap")
    ), object.getSource());
    assertEquals(propertyMap.get("scienceName").value().orElseThrow(
        () -> new IllegalStateException("scienceName not found in propertyMap")
    ), object.getScienceName());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "audio",
      "depth",
      "other"
  })
  void testConvertSensor(String type) throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "description", new ValueWithColumnNumber(Optional.of("test-description"), 2),
        "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 3),
        "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 4),
        "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 5),
        "type", new ValueWithColumnNumber(Optional.of(type), 6)
    ));
    
    if (type.equals("other")) {
      propertyMap.put(
          "properties", new ValueWithColumnNumber(Optional.of("test-properties"), 7)
      );
      propertyMap.put(
          "sensorType", new ValueWithColumnNumber(Optional.of("test-type"), 8)
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-hydrophoneId"), 7)
      );
      propertyMap.put(
          "preampId", new ValueWithColumnNumber(Optional.of("test-preampId"), 8)
      );
    }
    
    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1);
    assertEquals(propertyMap.get("uuid").value().orElseThrow(), sensor.getUuid().toString());
    assertEquals(propertyMap.get("name").value().orElseThrow(), sensor.getName());
    assertEquals(propertyMap.get("description").value().orElseThrow(), sensor.getDescription());
    assertEquals(propertyMap.get("position.x").value().orElseThrow(), sensor.getPosition().getX().toString());
    assertEquals(propertyMap.get("position.y").value().orElseThrow(), sensor.getPosition().getY().toString());
    assertEquals(propertyMap.get("position.z").value().orElseThrow(), sensor.getPosition().getZ().toString());
    
    if (type.equals("other")) {
      assertEquals(propertyMap.get("properties").value().orElseThrow(), ((OtherSensor) sensor).getProperties());
      assertEquals(propertyMap.get("sensorType").value().orElseThrow(), ((OtherSensor) sensor).getSensorType());
    } else if (type.equals("audio")) {
      assertEquals(propertyMap.get("hydrophoneId").value().orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
      assertEquals(propertyMap.get("preampId").value().orElseThrow(), ((AudioSensor) sensor).getPreampId());
    }
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void testConvertMissingProperty(Class<? extends ObjectWithName> clazz) throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(""), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1)
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz, 1);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").value().orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSourceMissingProperty() throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
        "source", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "scienceName", new ValueWithColumnNumber(Optional.of(""), 2)
    );

    DetectionType object = TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1);
    assertEquals(propertyMap.get("uuid").value().orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("source").value().orElseThrow(
        () -> new IllegalStateException("source not found in propertyMap")
    ), object.getSource());
    assertNull(object.getScienceName());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "audio",
      "depth",
      "other"
  })
  void testConvertMissingSensorProperty(String type) throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(""), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "description", new ValueWithColumnNumber(Optional.of("test-description"), 2),
        "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 3),
        "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 4),
        "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 5),
        "type", new ValueWithColumnNumber(Optional.of(type), 6)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", new ValueWithColumnNumber(Optional.of("test-properties"), 7)
      );
      propertyMap.put(
          "sensorType", new ValueWithColumnNumber(Optional.of("test-type"), 8)
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-hydrophoneId"), 7)
      );
      propertyMap.put(
          "preampId", new ValueWithColumnNumber(Optional.of("test-preampId"), 8)
      );
    }

    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1);
    assertNull(sensor.getUuid());
    assertEquals(propertyMap.get("name").value().orElseThrow(), sensor.getName());
    assertEquals(propertyMap.get("description").value().orElseThrow(), sensor.getDescription());
    assertEquals(propertyMap.get("position.x").value().orElseThrow(), sensor.getPosition().getX().toString());
    assertEquals(propertyMap.get("position.y").value().orElseThrow(), sensor.getPosition().getY().toString());
    assertEquals(propertyMap.get("position.z").value().orElseThrow(), sensor.getPosition().getZ().toString());

    if (type.equals("other")) {
      assertEquals(propertyMap.get("properties").value().orElseThrow(), ((OtherSensor) sensor).getProperties());
      assertEquals(propertyMap.get("sensorType").value().orElseThrow(), ((OtherSensor) sensor).getSensorType());
    } else if (type.equals("audio")) {
      assertEquals(propertyMap.get("hydrophoneId").value().orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
      assertEquals(propertyMap.get("preampId").value().orElseThrow(), ((AudioSensor) sensor).getPreampId());
    }
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvertNullUUID(Class<? extends ObjectWithName> clazz) throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1)
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz, 1);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").value().orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSourceNullUUID() throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 0),
        "source", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "scienceName", new ValueWithColumnNumber(Optional.of(""), 2)
    );

    DetectionType object = TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1);
    assertNull(object.getUuid());
    assertNull(object.getScienceName());
    assertEquals(propertyMap.get("source").value().orElseThrow(
        () -> new IllegalStateException("source not found in propertyMap")
    ), object.getSource());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "other",
      "audio",
      "depth"
  })
  void testConvertNullSensorUUID(String type) throws RowConversionException {
    Map<String, ValueWithColumnNumber> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "description", new ValueWithColumnNumber(Optional.of("test-description"), 2),
        "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 3),
        "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 4),
        "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 5),
        "type", new ValueWithColumnNumber(Optional.of(type), 6)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", new ValueWithColumnNumber(Optional.of("test-properties"), 7)
      );
      propertyMap.put(
          "sensorType", new ValueWithColumnNumber(Optional.of("test-type"), 8)
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-hydrophoneId"), 7)
      );
      propertyMap.put(
          "preampId", new ValueWithColumnNumber(Optional.of("test-preampId"), 8)
      );
    }

    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1);
    assertNull(sensor.getUuid());
    assertEquals(propertyMap.get("name").value().orElseThrow(), sensor.getName());
    assertEquals(propertyMap.get("description").value().orElseThrow(), sensor.getDescription());
    assertEquals(propertyMap.get("position.x").value().orElseThrow(), sensor.getPosition().getX().toString());
    assertEquals(propertyMap.get("position.y").value().orElseThrow(), sensor.getPosition().getY().toString());
    assertEquals(propertyMap.get("position.z").value().orElseThrow(), sensor.getPosition().getZ().toString());

    if (type.equals("other")) {
      assertEquals(propertyMap.get("properties").value().orElseThrow(), ((OtherSensor) sensor).getProperties());
      assertEquals(propertyMap.get("sensorType").value().orElseThrow(), ((OtherSensor) sensor).getSensorType());
    } else if (type.equals("audio")) {
      assertEquals(propertyMap.get("hydrophoneId").value().orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
      assertEquals(propertyMap.get("preampId").value().orElseThrow(), ((AudioSensor) sensor).getPreampId());
    }
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvertBadUUIDAndName(Class<? extends ObjectWithName> clazz) {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
        "name", new ValueWithColumnNumber(Optional.empty(), 1)
    );

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, clazz, 1));
    assertEquals("Translation failed", exception.getMessage());
    
    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

  @Test
  void testConvertSoundSourceBadUUIDAndName() {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
        "source", new ValueWithColumnNumber(Optional.empty(), 1),
        "scienceName", new ValueWithColumnNumber(Optional.of(""), 2)
    );

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> (v).getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "other",
      "audio",
      "depth"
  })
  void testConvertSensorBadUUIDAndName(String type) {
    Map<String, ValueWithColumnNumber> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
        "name", new ValueWithColumnNumber(Optional.empty(), 1),
        "description", new ValueWithColumnNumber(Optional.of("test-description"), 2),
        "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 3),
        "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 4),
        "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 5),
        "type", new ValueWithColumnNumber(Optional.of(type), 6)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", new ValueWithColumnNumber(Optional.of("test-properties"), 7)
      );
      propertyMap.put(
          "sensorType", new ValueWithColumnNumber(Optional.of("test-type"), 8)
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-hydrophoneId"), 7)
      );
      propertyMap.put(
          "preampId", new ValueWithColumnNumber(Optional.of("test-preampId"), 8)
      );
    }

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Project.class,
      Sea.class,
      Platform.class
  })
  void testConvertShipBadUUID(Class<? extends ObjectWithName> clazz) {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1)
    );

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, clazz, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

  @Test
  void testConvertSoundSourceBadUUID() {
    Map<String, ValueWithColumnNumber> propertyMap = Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
        "source", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "scienceName", new ValueWithColumnNumber(Optional.of(""), 2)
    );

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "other",
      "audio",
      "depth"
  })
  void testConvertSensorBadUUID(String type) {
    Map<String, ValueWithColumnNumber> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "description", new ValueWithColumnNumber(Optional.of("test-description"), 2),
        "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 3),
        "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 4),
        "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 5),
        "type", new ValueWithColumnNumber(Optional.of(type), 6)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", new ValueWithColumnNumber(Optional.of("test-properties"), 7)
      );
      propertyMap.put(
          "sensorType", new ValueWithColumnNumber(Optional.of("test-type"), 8)
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-hydrophoneId"), 7)
      );
      propertyMap.put(
          "preampId", new ValueWithColumnNumber(Optional.of("test-preampId"), 8)
      );
    }

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "depth",
      "audio",
      "other"
  })
  void testConvertSensorBadPosition(String type) {
    Map<String, ValueWithColumnNumber> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "description", new ValueWithColumnNumber(Optional.of("test-description"), 2),
        "position.x", new ValueWithColumnNumber(Optional.of("testX"), 3),
        "position.y", new ValueWithColumnNumber(Optional.of("testY"), 4),
        "position.z", new ValueWithColumnNumber(Optional.of("testZ"), 5),
        "type", new ValueWithColumnNumber(Optional.of(type), 6)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 7)
      );
      propertyMap.put(
          "sensorType", new ValueWithColumnNumber(Optional.of("test-name"), 8)
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-description"), 7)
      );
      propertyMap.put(
          "preampId", new ValueWithColumnNumber(Optional.of("testX"), 8)
      );
    }

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1));
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(3, exception.getSuppressed().length);
    FieldException violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("position.x"))
        .findFirst().orElseThrow();
    assertEquals("position.x", violation.getProperty());
    assertEquals("invalid decimal format", violation.getMessage());

    violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("position.y"))
        .findFirst().orElseThrow();
    assertEquals("position.y", violation.getProperty());
    assertEquals("invalid decimal format", violation.getMessage());

    violation = Arrays.stream(exception.getSuppressed())
        .map(v -> (FieldException) v)
        .filter(v -> v.getProperty().equals("position.z"))
        .findFirst().orElseThrow();
    assertEquals("position.z", violation.getProperty());
    assertEquals("invalid decimal format", violation.getMessage());
  }
  
  @Test
  void testConvertInvalidSensorType() {
    class TestSensor implements Sensor {
      @Override
      public String getName() {
        return "name";
      }

      @Override
      public Position getPosition() {
        return Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build();
      }

      @Override
      public String getDescription() {
        return "description";
      }

      @Override
      public UUID getUuid() {
        return UUID.randomUUID();
      }
    }

    List<String> fieldNames = FieldNameFactory.getSensorDeclaredFields(SensorType.depth);
    
    Map<String, ValueWithColumnNumber> propertyMap = IntStream.range(0, fieldNames.size()).boxed()
        .collect(Collectors.toMap(
            fieldNames::get,
            i -> new ValueWithColumnNumber(Optional.of(""), i)
        ));
    
    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, TestSensor.class, 1));
    assertEquals(String.format(
        "Translation not supported for %s", TestSensor.class.getSimpleName()
    ), exception.getMessage());
  }
  
  @Test
  void testTranslateInstrumentMissingRepository() {
    List<String> fieldNames = FieldNameFactory.getDefaultDeclaredFields(Instrument.class);
    Map<String, ValueWithColumnNumber> propertyMap = IntStream.range(0, fieldNames.size()).boxed()
        .collect(Collectors.toMap(
            fieldNames::get,
            i -> new ValueWithColumnNumber(Optional.of(""), i)
        ));
    Exception exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Instrument.class, 1, mock(PersonRepository.class)));
    assertEquals("Instrument translation missing fileType repository", exception.getMessage());

    exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Instrument.class, 1));
    assertEquals("Instrument translation missing fileType repository", exception.getMessage());
  }
  
  @Test
  void testTranslateInstrument() throws NotFoundException, DatastoreException, RowConversionException {
    FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);
    
    String fileType1 = "test-1";
    String fileType2 = "test-2";
    when(fileTypeRepository.getByUniqueField(fileType1)).thenReturn(FileType.builder()
            .type(fileType1)
            .comment(String.format(
                "%s-comment", fileType1 
            ))
        .build());
    when(fileTypeRepository.getByUniqueField(fileType2)).thenReturn(FileType.builder()
            .type(fileType2)
            .comment(String.format(
                "%s-comment", fileType2 
            ))
        .build());
    
    Instrument instrument = TranslatorUtils.convertMapToObject(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 1),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 2),
        "fileTypes", new ValueWithColumnNumber(Optional.of(String.format(
            "%s;%s", fileType1, fileType2
        )), 3)
    ), Instrument.class, 1, fileTypeRepository);
    
    assertNull(instrument.getUuid());
    assertEquals("test-name", instrument.getName());
    assertEquals(2, instrument.getFileTypes().size());
    assertEquals(fileType1, instrument.getFileTypes().get(0).getType());
    assertEquals(String.format(
        "%s-comment", fileType1
    ), instrument.getFileTypes().get(0).getComment());
    assertEquals(fileType2, instrument.getFileTypes().get(1).getType());
    assertEquals(String.format(
        "%s-comment", fileType2
    ), instrument.getFileTypes().get(1).getComment());
  }

  @Test
  void testTranslateInstrumentNoFileTypes() throws RowConversionException {
    FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);

    Instrument instrument = TranslatorUtils.convertMapToObject(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "fileTypes", new ValueWithColumnNumber(Optional.empty(), 2)
    ), Instrument.class, 1, fileTypeRepository);

    assertNull(instrument.getUuid());
    assertEquals("test-name", instrument.getName());
    assertEquals(0, instrument.getFileTypes().size());
  }
  
  @Test
  void testTranslateInstrumentBadUUID() {
    FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.of("TEST-UUID"), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "fileTypes", new ValueWithColumnNumber(Optional.empty(), 2)
    ), Instrument.class, 1, fileTypeRepository));
    
    assertEquals("Translation failed", exception.getMessage());
    Throwable[] exceptionCauses = exception.getSuppressed();
    assertEquals(1, exceptionCauses.length);
    Throwable exceptionCause = exceptionCauses[0];
    assertInstanceOf(FieldException.class, exceptionCause);
    FieldException fieldException = (FieldException) exceptionCause;
    assertEquals("uuid", fieldException.getProperty());
    assertEquals("invalid uuid format", fieldException.getMessage());
  }
  
  @Test
  void testTranslateInstrumentFileTypeNotFound() throws NotFoundException, DatastoreException {
    FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);

    String fileType1 = "test-1";
    String fileType2 = "test-2";
    when(fileTypeRepository.getByUniqueField(fileType1)).thenReturn(FileType.builder()
        .type(fileType1)
        .comment(String.format(
            "%s-comment", fileType1
        ))
        .build());
    when(fileTypeRepository.getByUniqueField(fileType2)).thenThrow(
        new NotFoundException(String.format(
            "file type with type %s not found", fileType2
        ))
    );

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "fileTypes", new ValueWithColumnNumber(Optional.of(String.format(
            "%s;%s", fileType1, fileType2
        )), 2)
    ), Instrument.class, 1, fileTypeRepository));
    assertEquals("Translation failed", exception.getMessage());
    Throwable[] exceptionCauses = exception.getSuppressed();
    assertEquals(1, exceptionCauses.length);
    Throwable exceptionCause = exceptionCauses[0];
    assertInstanceOf(FieldException.class, exceptionCause);
    FieldException notFoundException = (FieldException) exceptionCause;
    assertEquals(String.format(
        "file type with type %s not found", fileType2
    ), notFoundException.getMessage());
  }

  @Test
  void testTranslateInstrumentDatastoreFailure() throws NotFoundException, DatastoreException {
    FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);

    String fileType1 = "test-1";
    String fileType2 = "test-2";
    when(fileTypeRepository.getByUniqueField(fileType1)).thenReturn(FileType.builder()
        .type(fileType1)
        .comment(String.format(
            "%s-comment", fileType1
        ))
        .build());
    when(fileTypeRepository.getByUniqueField(fileType2)).thenThrow(
        new DatastoreException(String.format(
            "failed to retrieve file type with type %s", fileType2
        ), null)
    );

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "uuid", new ValueWithColumnNumber(Optional.empty(), 0),
        "name", new ValueWithColumnNumber(Optional.of("test-name"), 1),
        "fileTypes", new ValueWithColumnNumber(Optional.of(String.format(
            "%s;%s", fileType1, fileType2
        )), 2)
    ), Instrument.class, 1, fileTypeRepository));
    assertEquals("Translation failed", exception.getMessage());
    Throwable[] exceptionCauses = exception.getSuppressed();
    assertEquals(1, exceptionCauses.length);
    Throwable exceptionCause = exceptionCauses[0];
    assertInstanceOf(FieldException.class, exceptionCause);
    FieldException notFoundException = (FieldException) exceptionCause;
    assertEquals(String.format(
        "failed to retrieve file type with type %s", fileType2
    ), notFoundException.getMessage());
  }
  
  @Test
  void testTranslateAudioDataset() throws RowConversionException, NotFoundException, DatastoreException {
    
    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("temperaturePath", new ValueWithColumnNumber(Optional.of("temperaturePath"), 0));
    values.put("documentsPath", new ValueWithColumnNumber(Optional.of("documentsPath"), 0));
    values.put("otherPath", new ValueWithColumnNumber(Optional.of("otherPath"), 0));
    values.put("navigationPath", new ValueWithColumnNumber(Optional.of("navigationPath"), 0));
    values.put("calibrationDocumentsPath", new ValueWithColumnNumber(Optional.of("calibrationDocumentsPath"), 0));
    values.put("sourcePath", new ValueWithColumnNumber(Optional.of("sourcePath"), 0));
    values.put("biologicalPath", new ValueWithColumnNumber(Optional.of("biologicalPath"), 0));
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("audio"), 0));
    values.put("siteOrCruiseName", new ValueWithColumnNumber(Optional.of("site-or-cruise-name"), 0));
    values.put("deploymentId", new ValueWithColumnNumber(Optional.of("deployment-id"), 0));
    values.put("datasetPackager", new ValueWithColumnNumber(Optional.of("dataset-packager"), 0));
    values.put("projects", new ValueWithColumnNumber(Optional.of("project-1;project-2"), 0));
    values.put("publicReleaseDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 0));
    values.put("scientists", new ValueWithColumnNumber(Optional.of("scientist-1;scientist-2"), 0));
    values.put("sponsors", new ValueWithColumnNumber(Optional.of("sponsor-1;sponsor-2"), 0));
    values.put("funders", new ValueWithColumnNumber(Optional.of("funder-1;funder-2"), 0));
    values.put("platform", new ValueWithColumnNumber(Optional.of("platform"), 0));
    values.put("instrument", new ValueWithColumnNumber(Optional.of("instrument"), 0));
    values.put("instrumentId", new ValueWithColumnNumber(Optional.of("instrumentId"), 0));
    values.put("startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("preDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().minusDays(14).toString()), 0));
    values.put("postDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 0));
    values.put("calibrationDescription", new ValueWithColumnNumber(Optional.of("calibration-description"), 0));
    values.put("hydrophoneSensitivity", new ValueWithColumnNumber(Optional.of("10.0"), 0));
    values.put("frequencyRange", new ValueWithColumnNumber(Optional.of("5.0"), 0));
    values.put("gain", new ValueWithColumnNumber(Optional.of("1.0"), 0));
    values.put("deploymentTitle", new ValueWithColumnNumber(Optional.of("deployment-title"), 0));
    values.put("deploymentPurpose", new ValueWithColumnNumber(Optional.of("deployment-purpose"), 0));
    values.put("deploymentDescription", new ValueWithColumnNumber(Optional.of("deployment-description"), 0));
    values.put("alternateSiteName", new ValueWithColumnNumber(Optional.of("alternate-site-name"), 0));
    values.put("alternateDeploymentName", new ValueWithColumnNumber(Optional.of("alternate-deployment-name"), 0));
    values.put("qualityAnalyst", new ValueWithColumnNumber(Optional.of("quality-analyst"), 0));
    values.put("qualityAnalysisObjectives", new ValueWithColumnNumber(Optional.of("quality-analysis-objectives"), 0));
    values.put("qualityAnalysisMethod", new ValueWithColumnNumber(Optional.of("quality-analysis-method"), 0));
    values.put("qualityAssessmentDescription", new ValueWithColumnNumber(Optional.of("quality-assessment-description"), 0));
    values.put("qualityEntries[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 0));
    values.put("qualityEntries[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("qualityEntries[0].minFrequency", new ValueWithColumnNumber(Optional.of("100.0"), 0));
    values.put("qualityEntries[0].maxFrequency", new ValueWithColumnNumber(Optional.of("200.0"), 0));
    values.put("qualityEntries[0].qualityLevel", new ValueWithColumnNumber(Optional.of("Good"), 0));
    values.put("qualityEntries[0].comments", new ValueWithColumnNumber(Optional.of("quality-comment-1"), 0));
    values.put("qualityEntries[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 0));
    values.put("qualityEntries[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().plusDays(1).toString()), 0));
    values.put("qualityEntries[1].minFrequency", new ValueWithColumnNumber(Optional.of("300.0"), 0));
    values.put("qualityEntries[1].maxFrequency", new ValueWithColumnNumber(Optional.of("400.0"), 0));
    values.put("qualityEntries[1].qualityLevel", new ValueWithColumnNumber(Optional.of("Unusable"), 0));
    values.put("qualityEntries[1].comments", new ValueWithColumnNumber(Optional.of("quality-comment-2"), 0));
    values.put("deploymentTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(10).toString()), 0));
    values.put("recoveryTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("comments", new ValueWithColumnNumber(Optional.of("deployment-comments"), 0));
    values.put("sensors", new ValueWithColumnNumber(Optional.of("sensor-1;sensor-2"), 0));
    values.put("channels[0].sensor", new ValueWithColumnNumber(Optional.of("sensor-1"), 0));
    values.put("channels[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("channels[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[0].sampleRates[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[0].sampleRates[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("channels[0].sampleRates[0].sampleRate", new ValueWithColumnNumber(Optional.of("1.0"), 0));
    values.put("channels[0].sampleRates[0].sampleBits", new ValueWithColumnNumber(Optional.of("2"), 0));
    values.put("channels[0].sampleRates[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 0));
    values.put("channels[0].sampleRates[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 0));
    values.put("channels[0].sampleRates[1].sampleRate", new ValueWithColumnNumber(Optional.of("3.0"), 0));
    values.put("channels[0].sampleRates[1].sampleBits", new ValueWithColumnNumber(Optional.of("4"), 0));
    values.put("channels[0].dutyCycles[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(10).toString()), 0));
    values.put("channels[0].dutyCycles[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 0));
    values.put("channels[0].dutyCycles[0].duration", new ValueWithColumnNumber(Optional.of("10.0"), 0));
    values.put("channels[0].dutyCycles[0].interval", new ValueWithColumnNumber(Optional.of("11.0"), 0));
    values.put("channels[0].dutyCycles[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(8).toString()), 0));
    values.put("channels[0].dutyCycles[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(6).toString()), 0));
    values.put("channels[0].dutyCycles[1].duration", new ValueWithColumnNumber(Optional.of("12.0"), 0));
    values.put("channels[0].dutyCycles[1].interval", new ValueWithColumnNumber(Optional.of("13.0"), 0));
    values.put("channels[0].gains[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(100).toString()), 0));
    values.put("channels[0].gains[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(50).toString()), 0));
    values.put("channels[0].gains[0].gain", new ValueWithColumnNumber(Optional.of("100.0"), 0));
    values.put("channels[0].gains[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(200).toString()), 0));
    values.put("channels[0].gains[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(150).toString()), 0));
    values.put("channels[0].gains[1].gain", new ValueWithColumnNumber(Optional.of("200.0"), 0));
    values.put("channels[1].sensor", new ValueWithColumnNumber(Optional.of("sensor-2"), 0));
    values.put("channels[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(21).toString()), 0));
    values.put("channels[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("channels[1].sampleRates[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("channels[1].sampleRates[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[1].sampleRates[0].sampleRate", new ValueWithColumnNumber(Optional.of("5.0"), 0));
    values.put("channels[1].sampleRates[0].sampleBits", new ValueWithColumnNumber(Optional.of("6"), 0));
    values.put("channels[1].sampleRates[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(4).toString()), 0));
    values.put("channels[1].sampleRates[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(3).toString()), 0));
    values.put("channels[1].sampleRates[1].sampleRate", new ValueWithColumnNumber(Optional.of("7.0"), 0));
    values.put("channels[1].sampleRates[1].sampleBits", new ValueWithColumnNumber(Optional.of("8"), 0));
    values.put("channels[1].dutyCycles[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(11).toString()), 0));
    values.put("channels[1].dutyCycles[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(3).toString()), 0));
    values.put("channels[1].dutyCycles[0].duration", new ValueWithColumnNumber(Optional.of("11.0"), 0));
    values.put("channels[1].dutyCycles[0].interval", new ValueWithColumnNumber(Optional.of("12.0"), 0));
    values.put("channels[1].dutyCycles[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(9).toString()), 0));
    values.put("channels[1].dutyCycles[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[1].dutyCycles[1].duration", new ValueWithColumnNumber(Optional.of("13.0"), 0));
    values.put("channels[1].dutyCycles[1].interval", new ValueWithColumnNumber(Optional.of("14.0"), 0));
    values.put("channels[1].gains[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(101).toString()), 0));
    values.put("channels[1].gains[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(51).toString()), 0));
    values.put("channels[1].gains[0].gain", new ValueWithColumnNumber(Optional.of("101.0"), 0));
    values.put("channels[1].gains[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(201).toString()), 0));
    values.put("channels[1].gains[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(151).toString()), 0));
    values.put("channels[1].gains[1].gain", new ValueWithColumnNumber(Optional.of("201.0"), 0));
    
    values = createDatasetMap(DatasetType.AUDIO, LocationType.MOBILE_MARINE, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);
    
    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(Person.builder()
            .name("dataset-packager")
        .build());
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(Person.builder()
            .name("scientist-1")
        .build());
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(Person.builder()
            .name("scientist-2")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-1")).thenReturn(Organization.builder()
            .name("sponsor-1")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-2")).thenReturn(Organization.builder()
            .name("sponsor-2")
        .build());
    when(organizationRepository.getByUniqueField("funder-1")).thenReturn(Organization.builder()
            .name("funder-1")
        .build());
    when(organizationRepository.getByUniqueField("funder-2")).thenReturn(Organization.builder()
            .name("funder-2")
        .build());
    when(projectRepository.getByUniqueField("project-1")).thenReturn(Project.builder()
            .name("project-1")
        .build());
    when(projectRepository.getByUniqueField("project-2")).thenReturn(Project.builder()
            .name("project-2")
        .build());
    when(platformRepository.getByUniqueField("platform")).thenReturn(Platform.builder()
            .name("platform")
        .build());
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(Instrument.builder()
            .name("instrument")
        .build());
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(Person.builder()
            .name("quality-analyst")
        .build());
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(DepthSensor.builder()
            .name("sensor-1")
        .build());
    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(AudioSensor.builder()
            .name("sensor-2")
        .build());
    
    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
          sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );
    
    assertInstanceOf(AudioPackage.class, packingJob);
    
    AudioPackage audioPackingJob = (AudioPackage) packingJob;
    assertEquals(values.get("temperaturePath").value().orElseThrow(), audioPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").value().orElseThrow(), audioPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").value().orElseThrow(), audioPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").value().orElseThrow(), audioPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").value().orElseThrow(), audioPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").value().orElseThrow(), audioPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").value().orElseThrow(), audioPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").value().orElseThrow(), audioPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").value().orElseThrow(), audioPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").value().orElseThrow(), audioPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").value().orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getProjects().get(0).getName(), audioPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").value().orElseThrow(), audioPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").value().orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getScientists().get(0).getName(), audioPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").value().orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getSponsors().get(0).getName(), audioPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").value().orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getFunders().get(0).getName(), audioPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").value().orElseThrow(), audioPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").value().orElseThrow(), audioPackingJob.getInstrument().getName());
    assertEquals(values.get("instrumentId").value().orElseThrow(), audioPackingJob.getInstrumentId());
    assertEquals(values.get("startTime").value().orElseThrow(), audioPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").value().orElseThrow(), audioPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").value().orElseThrow(), audioPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").value().orElseThrow(), audioPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").value().orElseThrow(), audioPackingJob.getCalibrationDescription());
    assertEquals(values.get("hydrophoneSensitivity").value().orElseThrow(), audioPackingJob.getHydrophoneSensitivity().toString());
    assertEquals(values.get("frequencyRange").value().orElseThrow(), audioPackingJob.getFrequencyRange().toString());
    assertEquals(values.get("gain").value().orElseThrow(), audioPackingJob.getGain().toString());
    assertEquals(values.get("deploymentTitle").value().orElseThrow(), audioPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").value().orElseThrow(), audioPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").value().orElseThrow(), audioPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").value().orElseThrow(), audioPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").value().orElseThrow(), audioPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("qualityAnalyst").value().orElseThrow(), audioPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").value().orElseThrow(), audioPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").value().orElseThrow(), audioPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").value().orElseThrow(), audioPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").value().orElseThrow(), audioPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").value().orElseThrow(), audioPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").value().orElseThrow(), audioPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").value().orElseThrow(), audioPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").value().orElseThrow(), audioPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").value().orElseThrow(), audioPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").value().orElseThrow(), audioPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").value().orElseThrow(), audioPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").value().orElseThrow(), audioPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").value().orElseThrow(), audioPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").value().orElseThrow(), audioPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").value().orElseThrow(), audioPackingJob.getQualityEntries().get(1).getComments());
    assertEquals(values.get("deploymentTime").value().orElseThrow(), audioPackingJob.getDeploymentTime().toString());
    assertEquals(values.get("recoveryTime").value().orElseThrow(), audioPackingJob.getRecoveryTime().toString());
    assertEquals(values.get("comments").value().orElseThrow(), audioPackingJob.getComments());
    assertEquals(values.get("sensors").value().orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getSensors().get(0).getName(), audioPackingJob.getSensors().get(1).getName()
    ));
    assertEquals(values.get("channels[0].sensor").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSensor().getName());
    assertEquals(values.get("channels[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleRate").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleBits").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[0].sampleRates[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleRate").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleBits").value().orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].duration").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].interval").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].duration").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].interval").value().orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[0].gains[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[0].gain").value().orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[0].gains[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[1].gain").value().orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(1).getGain().toString());
    assertEquals(values.get("channels[1].sensor").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSensor().getName());
    assertEquals(values.get("channels[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleRate").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleBits").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[1].sampleRates[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleRate").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleBits").value().orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].duration").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].interval").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].duration").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].interval").value().orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[1].gains[0].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[0].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[0].gain").value().orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[1].gains[1].startTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[1].endTime").value().orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[1].gain").value().orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(1).getGain().toString());
  }

  @Test
  void testTranslateCPODDataset() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.CPOD, LocationType.MOBILE_MARINE, new HashMap<>(0));
    values.put("temperaturePath", new ValueWithColumnNumber(Optional.of("temperaturePath"), 0));
    values.put("documentsPath", new ValueWithColumnNumber(Optional.of("documentsPath"), 0));
    values.put("otherPath", new ValueWithColumnNumber(Optional.of("otherPath"), 0));
    values.put("navigationPath", new ValueWithColumnNumber(Optional.of("navigationPath"), 0));
    values.put("calibrationDocumentsPath", new ValueWithColumnNumber(Optional.of("calibrationDocumentsPath"), 0));
    values.put("sourcePath", new ValueWithColumnNumber(Optional.of("sourcePath"), 0));
    values.put("biologicalPath", new ValueWithColumnNumber(Optional.of("biologicalPath"), 0));
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("CPOD"), 0));
    values.put("siteOrCruiseName", new ValueWithColumnNumber(Optional.of("site-or-cruise-name"), 0));
    values.put("deploymentId", new ValueWithColumnNumber(Optional.of("deployment-id"), 0));
    values.put("datasetPackager", new ValueWithColumnNumber(Optional.of("dataset-packager"), 0));
    values.put("projects", new ValueWithColumnNumber(Optional.of("project-1;project-2"), 0));
    values.put("publicReleaseDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 0));
    values.put("scientists", new ValueWithColumnNumber(Optional.of("scientist-1;scientist-2"), 0));
    values.put("sponsors", new ValueWithColumnNumber(Optional.of("sponsor-1;sponsor-2"), 0));
    values.put("funders", new ValueWithColumnNumber(Optional.of("funder-1;funder-2"), 0));
    values.put("platform", new ValueWithColumnNumber(Optional.of("platform"), 0));
    values.put("instrument", new ValueWithColumnNumber(Optional.of("instrument"), 0));
    values.put("instrumentId", new ValueWithColumnNumber(Optional.of("instrumentId"), 0));
    values.put("startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("preDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().minusDays(14).toString()), 0));
    values.put("postDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 0));
    values.put("calibrationDescription", new ValueWithColumnNumber(Optional.of("calibration-description"), 0));
    values.put("hydrophoneSensitivity", new ValueWithColumnNumber(Optional.of("10.0"), 0));
    values.put("frequencyRange", new ValueWithColumnNumber(Optional.of("5.0"), 0));
    values.put("gain", new ValueWithColumnNumber(Optional.of("1.0"), 0));
    values.put("deploymentTitle", new ValueWithColumnNumber(Optional.of("deployment-title"), 0));
    values.put("deploymentPurpose", new ValueWithColumnNumber(Optional.of("deployment-purpose"), 0));
    values.put("deploymentDescription", new ValueWithColumnNumber(Optional.of("deployment-description"), 0));
    values.put("alternateSiteName", new ValueWithColumnNumber(Optional.of("alternate-site-name"), 0));
    values.put("alternateDeploymentName", new ValueWithColumnNumber(Optional.of("alternate-deployment-name"), 0));
    values.put("qualityAnalyst", new ValueWithColumnNumber(Optional.of("quality-analyst"), 0));
    values.put("qualityAnalysisObjectives", new ValueWithColumnNumber(Optional.of("quality-analysis-objectives"), 0));
    values.put("qualityAnalysisMethod", new ValueWithColumnNumber(Optional.of("quality-analysis-method"), 0));
    values.put("qualityAssessmentDescription", new ValueWithColumnNumber(Optional.of("quality-assessment-description"), 0));
    values.put("qualityEntries[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 0));
    values.put("qualityEntries[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("qualityEntries[0].minFrequency", new ValueWithColumnNumber(Optional.of("100.0"), 0));
    values.put("qualityEntries[0].maxFrequency", new ValueWithColumnNumber(Optional.of("200.0"), 0));
    values.put("qualityEntries[0].qualityLevel", new ValueWithColumnNumber(Optional.of("Good"), 0));
    values.put("qualityEntries[0].comments", new ValueWithColumnNumber(Optional.of("quality-comment-1"), 0));
    values.put("qualityEntries[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 0));
    values.put("qualityEntries[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().plusDays(1).toString()), 0));
    values.put("qualityEntries[1].minFrequency", new ValueWithColumnNumber(Optional.of("300.0"), 0));
    values.put("qualityEntries[1].maxFrequency", new ValueWithColumnNumber(Optional.of("400.0"), 0));
    values.put("qualityEntries[1].qualityLevel", new ValueWithColumnNumber(Optional.of("Unusable"), 0));
    values.put("qualityEntries[1].comments", new ValueWithColumnNumber(Optional.of("quality-comment-2"), 0));
    values.put("deploymentTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(10).toString()), 0));
    values.put("recoveryTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("comments", new ValueWithColumnNumber(Optional.of("deployment-comments"), 0));
    values.put("sensors", new ValueWithColumnNumber(Optional.of("sensor-1;sensor-2"), 0));
    values.put("channels[0].sensor", new ValueWithColumnNumber(Optional.of("sensor-1"), 0));
    values.put("channels[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("channels[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[0].sampleRates[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[0].sampleRates[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 0));
    values.put("channels[0].sampleRates[0].sampleRate", new ValueWithColumnNumber(Optional.of("1.0"), 0));
    values.put("channels[0].sampleRates[0].sampleBits", new ValueWithColumnNumber(Optional.of("2"), 0));
    values.put("channels[0].sampleRates[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 0));
    values.put("channels[0].sampleRates[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 0));
    values.put("channels[0].sampleRates[1].sampleRate", new ValueWithColumnNumber(Optional.of("3.0"), 0));
    values.put("channels[0].sampleRates[1].sampleBits", new ValueWithColumnNumber(Optional.of("4"), 0));
    values.put("channels[0].dutyCycles[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(10).toString()), 0));
    values.put("channels[0].dutyCycles[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 0));
    values.put("channels[0].dutyCycles[0].duration", new ValueWithColumnNumber(Optional.of("10.0"), 0));
    values.put("channels[0].dutyCycles[0].interval", new ValueWithColumnNumber(Optional.of("11.0"), 0));
    values.put("channels[0].dutyCycles[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(8).toString()), 0));
    values.put("channels[0].dutyCycles[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(6).toString()), 0));
    values.put("channels[0].dutyCycles[1].duration", new ValueWithColumnNumber(Optional.of("12.0"), 0));
    values.put("channels[0].dutyCycles[1].interval", new ValueWithColumnNumber(Optional.of("13.0"), 0));
    values.put("channels[0].gains[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(100).toString()), 0));
    values.put("channels[0].gains[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(50).toString()), 0));
    values.put("channels[0].gains[0].gain", new ValueWithColumnNumber(Optional.of("100.0"), 0));
    values.put("channels[0].gains[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(200).toString()), 0));
    values.put("channels[0].gains[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(150).toString()), 0));
    values.put("channels[0].gains[1].gain", new ValueWithColumnNumber(Optional.of("200.0"), 0));
    values.put("channels[1].sensor", new ValueWithColumnNumber(Optional.of("sensor-2"), 0));
    values.put("channels[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(21).toString()), 0));
    values.put("channels[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("channels[1].sampleRates[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 0));
    values.put("channels[1].sampleRates[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[1].sampleRates[0].sampleRate", new ValueWithColumnNumber(Optional.of("5.0"), 0));
    values.put("channels[1].sampleRates[0].sampleBits", new ValueWithColumnNumber(Optional.of("6"), 0));
    values.put("channels[1].sampleRates[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(4).toString()), 0));
    values.put("channels[1].sampleRates[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(3).toString()), 0));
    values.put("channels[1].sampleRates[1].sampleRate", new ValueWithColumnNumber(Optional.of("7.0"), 0));
    values.put("channels[1].sampleRates[1].sampleBits", new ValueWithColumnNumber(Optional.of("8"), 0));
    values.put("channels[1].dutyCycles[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(11).toString()), 0));
    values.put("channels[1].dutyCycles[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(3).toString()), 0));
    values.put("channels[1].dutyCycles[0].duration", new ValueWithColumnNumber(Optional.of("11.0"), 0));
    values.put("channels[1].dutyCycles[0].interval", new ValueWithColumnNumber(Optional.of("12.0"), 0));
    values.put("channels[1].dutyCycles[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(9).toString()), 0));
    values.put("channels[1].dutyCycles[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(7).toString()), 0));
    values.put("channels[1].dutyCycles[1].duration", new ValueWithColumnNumber(Optional.of("13.0"), 0));
    values.put("channels[1].dutyCycles[1].interval", new ValueWithColumnNumber(Optional.of("14.0"), 0));
    values.put("channels[1].gains[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(101).toString()), 0));
    values.put("channels[1].gains[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(51).toString()), 0));
    values.put("channels[1].gains[0].gain", new ValueWithColumnNumber(Optional.of("101.0"), 0));
    values.put("channels[1].gains[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(201).toString()), 0));
    values.put("channels[1].gains[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(151).toString()), 0));
    values.put("channels[1].gains[1].gain", new ValueWithColumnNumber(Optional.of("201.0"), 0));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(Person.builder()
        .name("dataset-packager")
        .build());
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(Person.builder()
        .name("scientist-1")
        .build());
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(Person.builder()
        .name("scientist-2")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-1")).thenReturn(Organization.builder()
        .name("sponsor-1")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-2")).thenReturn(Organization.builder()
        .name("sponsor-2")
        .build());
    when(organizationRepository.getByUniqueField("funder-1")).thenReturn(Organization.builder()
        .name("funder-1")
        .build());
    when(organizationRepository.getByUniqueField("funder-2")).thenReturn(Organization.builder()
        .name("funder-2")
        .build());
    when(projectRepository.getByUniqueField("project-1")).thenReturn(Project.builder()
        .name("project-1")
        .build());
    when(projectRepository.getByUniqueField("project-2")).thenReturn(Project.builder()
        .name("project-2")
        .build());
    when(platformRepository.getByUniqueField("platform")).thenReturn(Platform.builder()
        .name("platform")
        .build());
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(Instrument.builder()
        .name("instrument")
        .build());
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(Person.builder()
        .name("quality-analyst")
        .build());
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(DepthSensor.builder()
        .name("sensor-1")
        .build());
    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(AudioSensor.builder()
        .name("sensor-2")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(CPODPackage.class, packingJob);

    CPODPackage cpodPackingJob = (CPODPackage) packingJob;
    assertEquals(values.get("temperaturePath").value().orElseThrow(), cpodPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").value().orElseThrow(), cpodPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").value().orElseThrow(), cpodPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").value().orElseThrow(), cpodPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").value().orElseThrow(), cpodPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").value().orElseThrow(), cpodPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").value().orElseThrow(), cpodPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").value().orElseThrow(), cpodPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").value().orElseThrow(), cpodPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").value().orElseThrow(), cpodPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").value().orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getProjects().get(0).getName(), cpodPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").value().orElseThrow(), cpodPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").value().orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getScientists().get(0).getName(), cpodPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").value().orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getSponsors().get(0).getName(), cpodPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").value().orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getFunders().get(0).getName(), cpodPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").value().orElseThrow(), cpodPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").value().orElseThrow(), cpodPackingJob.getInstrument().getName());
    assertEquals(values.get("instrumentId").value().orElseThrow(), cpodPackingJob.getInstrumentId());
    assertEquals(values.get("startTime").value().orElseThrow(), cpodPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").value().orElseThrow(), cpodPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").value().orElseThrow(), cpodPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").value().orElseThrow(), cpodPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").value().orElseThrow(), cpodPackingJob.getCalibrationDescription());
    assertEquals(values.get("hydrophoneSensitivity").value().orElseThrow(), cpodPackingJob.getHydrophoneSensitivity().toString());
    assertEquals(values.get("frequencyRange").value().orElseThrow(), cpodPackingJob.getFrequencyRange().toString());
    assertEquals(values.get("gain").value().orElseThrow(), cpodPackingJob.getGain().toString());
    assertEquals(values.get("deploymentTitle").value().orElseThrow(), cpodPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").value().orElseThrow(), cpodPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").value().orElseThrow(), cpodPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").value().orElseThrow(), cpodPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").value().orElseThrow(), cpodPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("qualityAnalyst").value().orElseThrow(), cpodPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").value().orElseThrow(), cpodPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").value().orElseThrow(), cpodPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").value().orElseThrow(), cpodPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").value().orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getComments());
    assertEquals(values.get("deploymentTime").value().orElseThrow(), cpodPackingJob.getDeploymentTime().toString());
    assertEquals(values.get("recoveryTime").value().orElseThrow(), cpodPackingJob.getRecoveryTime().toString());
    assertEquals(values.get("comments").value().orElseThrow(), cpodPackingJob.getComments());
    assertEquals(values.get("sensors").value().orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getSensors().get(0).getName(), cpodPackingJob.getSensors().get(1).getName()
    ));
    assertEquals(values.get("channels[0].sensor").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSensor().getName());
    assertEquals(values.get("channels[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleRate").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleBits").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[0].sampleRates[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleRate").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleBits").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].duration").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].interval").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].duration").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].interval").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[0].gains[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[0].gain").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[0].gains[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[1].gain").value().orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(1).getGain().toString());
    assertEquals(values.get("channels[1].sensor").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSensor().getName());
    assertEquals(values.get("channels[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleRate").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleBits").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[1].sampleRates[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleRate").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleBits").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].duration").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].interval").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].duration").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].interval").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[1].gains[0].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[0].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[0].gain").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[1].gains[1].startTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[1].endTime").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[1].gain").value().orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(1).getGain().toString());
  }

  @Test
  void testTranslateSoundClipsDataset() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("temperaturePath", new ValueWithColumnNumber(Optional.of("temperaturePath"), 1));
    values.put("documentsPath", new ValueWithColumnNumber(Optional.of("documentsPath"), 1));
    values.put("otherPath", new ValueWithColumnNumber(Optional.of("otherPath"), 1));
    values.put("navigationPath", new ValueWithColumnNumber(Optional.of("navigationPath"), 1));
    values.put("calibrationDocumentsPath", new ValueWithColumnNumber(Optional.of("calibrationDocumentsPath"), 1));
    values.put("sourcePath", new ValueWithColumnNumber(Optional.of("sourcePath"), 1));
    values.put("biologicalPath", new ValueWithColumnNumber(Optional.of("biologicalPath"), 1));
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("sound clips"), 1));
    values.put("siteOrCruiseName", new ValueWithColumnNumber(Optional.of("site-or-cruise-name"), 1));
    values.put("deploymentId", new ValueWithColumnNumber(Optional.of("deployment-id"), 1));
    values.put("datasetPackager", new ValueWithColumnNumber(Optional.of("dataset-packager"), 1));
    values.put("projects", new ValueWithColumnNumber(Optional.of("project-1;project-2"), 1));
    values.put("publicReleaseDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("scientists", new ValueWithColumnNumber(Optional.of("scientist-1;scientist-2"), 1));
    values.put("sponsors", new ValueWithColumnNumber(Optional.of("sponsor-1;sponsor-2"), 1));
    values.put("funders", new ValueWithColumnNumber(Optional.of("funder-1;funder-2"), 1));
    values.put("platform", new ValueWithColumnNumber(Optional.of("platform"), 1));
    values.put("instrument", new ValueWithColumnNumber(Optional.of("instrument"), 1));
    values.put("startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 1));
    values.put("endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 1));
    values.put("preDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().minusDays(14).toString()), 1));
    values.put("postDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("calibrationDescription", new ValueWithColumnNumber(Optional.of("calibration-description"), 1));
    values.put("deploymentTitle", new ValueWithColumnNumber(Optional.of("deployment-title"), 1));
    values.put("deploymentPurpose", new ValueWithColumnNumber(Optional.of("deployment-purpose"), 1));
    values.put("deploymentDescription", new ValueWithColumnNumber(Optional.of("deployment-description"), 1));
    values.put("alternateSiteName", new ValueWithColumnNumber(Optional.of("alternate-site-name"), 1));
    values.put("alternateDeploymentName", new ValueWithColumnNumber(Optional.of("alternate-deployment-name"), 1));
    values.put("softwareNames", new ValueWithColumnNumber(Optional.of("software-names"), 1));
    values.put("softwareVersions", new ValueWithColumnNumber(Optional.of("software-versions"), 1));
    values.put("softwareProtocolCitation", new ValueWithColumnNumber(Optional.of("software-protocol-citation"), 1));
    values.put("softwareDescription", new ValueWithColumnNumber(Optional.of("software-description"), 1));
    values.put("softwareProcessingDescription", new ValueWithColumnNumber(Optional.of("software-processing-description"), 1));
    values = createDatasetMap(DatasetType.SOUND_CLIPS, LocationType.MOBILE_MARINE, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(Person.builder()
        .name("dataset-packager")
        .build());
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(Person.builder()
        .name("scientist-1")
        .build());
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(Person.builder()
        .name("scientist-2")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-1")).thenReturn(Organization.builder()
        .name("sponsor-1")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-2")).thenReturn(Organization.builder()
        .name("sponsor-2")
        .build());
    when(organizationRepository.getByUniqueField("funder-1")).thenReturn(Organization.builder()
        .name("funder-1")
        .build());
    when(organizationRepository.getByUniqueField("funder-2")).thenReturn(Organization.builder()
        .name("funder-2")
        .build());
    when(projectRepository.getByUniqueField("project-1")).thenReturn(Project.builder()
        .name("project-1")
        .build());
    when(projectRepository.getByUniqueField("project-2")).thenReturn(Project.builder()
        .name("project-2")
        .build());
    when(platformRepository.getByUniqueField("platform")).thenReturn(Platform.builder()
        .name("platform")
        .build());
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(Instrument.builder()
        .name("instrument")
        .build());
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(Person.builder()
        .name("quality-analyst")
        .build());
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(DepthSensor.builder()
        .name("sensor-1")
        .build());
    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(AudioSensor.builder()
        .name("sensor-2")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(SoundClipsPackage.class, packingJob);

    SoundClipsPackage soundClipsPackingJob = (SoundClipsPackage) packingJob;
    assertEquals(values.get("temperaturePath").value().orElseThrow(), soundClipsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").value().orElseThrow(), soundClipsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").value().orElseThrow(), soundClipsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").value().orElseThrow(), soundClipsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").value().orElseThrow(), soundClipsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").value().orElseThrow(), soundClipsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").value().orElseThrow(), soundClipsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").value().orElseThrow(), soundClipsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").value().orElseThrow(), soundClipsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").value().orElseThrow(), soundClipsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").value().orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getProjects().get(0).getName(), soundClipsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").value().orElseThrow(), soundClipsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").value().orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getScientists().get(0).getName(), soundClipsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").value().orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getSponsors().get(0).getName(), soundClipsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").value().orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getFunders().get(0).getName(), soundClipsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").value().orElseThrow(), soundClipsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").value().orElseThrow(), soundClipsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").value().orElseThrow(), soundClipsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").value().orElseThrow(), soundClipsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").value().orElseThrow(), soundClipsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").value().orElseThrow(), soundClipsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").value().orElseThrow(), soundClipsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").value().orElseThrow(), soundClipsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").value().orElseThrow(), soundClipsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").value().orElseThrow(), soundClipsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").value().orElseThrow(), soundClipsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").value().orElseThrow(), soundClipsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").value().orElseThrow(), soundClipsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").value().orElseThrow(), soundClipsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").value().orElseThrow(), soundClipsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").value().orElseThrow(), soundClipsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").value().orElseThrow(), soundClipsPackingJob.getSoftwareProcessingDescription());
  }

  @Test
  void testTranslateDetections() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("temperaturePath", new ValueWithColumnNumber(Optional.of("temperaturePath"), 1));
    values.put("documentsPath", new ValueWithColumnNumber(Optional.of("documentsPath"), 1));
    values.put("otherPath", new ValueWithColumnNumber(Optional.of("otherPath"), 1));
    values.put("navigationPath", new ValueWithColumnNumber(Optional.of("navigationPath"), 1));
    values.put("calibrationDocumentsPath", new ValueWithColumnNumber(Optional.of("calibrationDocumentsPath"), 1));
    values.put("sourcePath", new ValueWithColumnNumber(Optional.of("sourcePath"), 1));
    values.put("biologicalPath", new ValueWithColumnNumber(Optional.of("biologicalPath"), 1));
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("detections"), 1));
    values.put("siteOrCruiseName", new ValueWithColumnNumber(Optional.of("site-or-cruise-name"), 1));
    values.put("deploymentId", new ValueWithColumnNumber(Optional.of("deployment-id"), 1));
    values.put("datasetPackager", new ValueWithColumnNumber(Optional.of("dataset-packager"), 1));
    values.put("projects", new ValueWithColumnNumber(Optional.of("project-1;project-2"), 1));
    values.put("publicReleaseDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("scientists", new ValueWithColumnNumber(Optional.of("scientist-1;scientist-2"), 1));
    values.put("sponsors", new ValueWithColumnNumber(Optional.of("sponsor-1;sponsor-2"), 1));
    values.put("funders", new ValueWithColumnNumber(Optional.of("funder-1;funder-2"), 1));
    values.put("platform", new ValueWithColumnNumber(Optional.of("platform"), 1));
    values.put("instrument", new ValueWithColumnNumber(Optional.of("instrument"), 1));
    values.put("startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 1));
    values.put("endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 1));
    values.put("preDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().minusDays(14).toString()), 1));
    values.put("postDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("calibrationDescription", new ValueWithColumnNumber(Optional.of("calibration-description"), 1));
    values.put("deploymentTitle", new ValueWithColumnNumber(Optional.of("deployment-title"), 1));
    values.put("deploymentPurpose", new ValueWithColumnNumber(Optional.of("deployment-purpose"), 1));
    values.put("deploymentDescription", new ValueWithColumnNumber(Optional.of("deployment-description"), 1));
    values.put("alternateSiteName", new ValueWithColumnNumber(Optional.of("alternate-site-name"), 1));
    values.put("alternateDeploymentName", new ValueWithColumnNumber(Optional.of("alternate-deployment-name"), 1));
    values.put("softwareNames", new ValueWithColumnNumber(Optional.of("software-names"), 1));
    values.put("softwareVersions", new ValueWithColumnNumber(Optional.of("software-versions"), 1));
    values.put("softwareProtocolCitation", new ValueWithColumnNumber(Optional.of("software-protocol-citation"), 1));
    values.put("softwareDescription", new ValueWithColumnNumber(Optional.of("software-description"), 1));
    values.put("softwareProcessingDescription", new ValueWithColumnNumber(Optional.of("software-processing-description"), 1));
    values.put("qualityAnalyst", new ValueWithColumnNumber(Optional.of("quality-analyst"), 1));
    values.put("qualityAnalysisObjectives", new ValueWithColumnNumber(Optional.of("quality-analysis-objectives"), 1));
    values.put("qualityAnalysisMethod", new ValueWithColumnNumber(Optional.of("quality-analysis-method"), 1));
    values.put("qualityAssessmentDescription", new ValueWithColumnNumber(Optional.of("quality-assessment-description"), 1));
    values.put("qualityEntries[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 1));
    values.put("qualityEntries[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 1));
    values.put("qualityEntries[0].minFrequency", new ValueWithColumnNumber(Optional.of("100.0"), 1));
    values.put("qualityEntries[0].maxFrequency", new ValueWithColumnNumber(Optional.of("200.0"), 1));
    values.put("qualityEntries[0].qualityLevel", new ValueWithColumnNumber(Optional.of("Good"), 1));
    values.put("qualityEntries[0].comments", new ValueWithColumnNumber(Optional.of("quality-comment-1"), 1));
    values.put("qualityEntries[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 1));
    values.put("qualityEntries[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().plusDays(1).toString()), 1));
    values.put("qualityEntries[1].minFrequency", new ValueWithColumnNumber(Optional.of("300.0"), 1));
    values.put("qualityEntries[1].maxFrequency", new ValueWithColumnNumber(Optional.of("400.0"), 1));
    values.put("qualityEntries[1].qualityLevel", new ValueWithColumnNumber(Optional.of("Unusable"), 1));
    values.put("qualityEntries[1].comments", new ValueWithColumnNumber(Optional.of("quality-comment-2"), 1));
    values.put("soundSource", new ValueWithColumnNumber(Optional.of("sound-source"), 1));
    values.put("analysisTimeZone", new ValueWithColumnNumber(Optional.of("1"), 1));
    values.put("analysisEffort", new ValueWithColumnNumber(Optional.of("2"), 1));
    values.put("sampleRate", new ValueWithColumnNumber(Optional.of("3.0"), 1));
    values.put("minFrequency", new ValueWithColumnNumber(Optional.of("4.0"), 1));
    values.put("maxFrequency", new ValueWithColumnNumber(Optional.of("5.0"), 1));
    values = createDatasetMap(DatasetType.DETECTIONS, LocationType.STATIONARY_MARINE, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(Person.builder()
        .name("dataset-packager")
        .build());
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(Person.builder()
        .name("scientist-1")
        .build());
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(Person.builder()
        .name("scientist-2")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-1")).thenReturn(Organization.builder()
        .name("sponsor-1")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-2")).thenReturn(Organization.builder()
        .name("sponsor-2")
        .build());
    when(organizationRepository.getByUniqueField("funder-1")).thenReturn(Organization.builder()
        .name("funder-1")
        .build());
    when(organizationRepository.getByUniqueField("funder-2")).thenReturn(Organization.builder()
        .name("funder-2")
        .build());
    when(projectRepository.getByUniqueField("project-1")).thenReturn(Project.builder()
        .name("project-1")
        .build());
    when(projectRepository.getByUniqueField("project-2")).thenReturn(Project.builder()
        .name("project-2")
        .build());
    when(platformRepository.getByUniqueField("platform")).thenReturn(Platform.builder()
        .name("platform")
        .build());
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(Instrument.builder()
        .name("instrument")
        .build());
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(Person.builder()
        .name("quality-analyst")
        .build());
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(DepthSensor.builder()
        .name("sensor-1")
        .build());
    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(AudioSensor.builder()
        .name("sensor-2")
        .build());
    when(detectionTypeRepository.getByUniqueField("sound-source")).thenReturn(DetectionType.builder()
            .source("sound-source")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(DetectionsPackage.class, packingJob);

    DetectionsPackage detectionsPackingJob = (DetectionsPackage) packingJob;
    assertEquals(values.get("temperaturePath").value().orElseThrow(), detectionsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").value().orElseThrow(), detectionsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").value().orElseThrow(), detectionsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").value().orElseThrow(), detectionsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").value().orElseThrow(), detectionsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").value().orElseThrow(), detectionsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").value().orElseThrow(), detectionsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").value().orElseThrow(), detectionsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").value().orElseThrow(), detectionsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").value().orElseThrow(), detectionsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").value().orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getProjects().get(0).getName(), detectionsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").value().orElseThrow(), detectionsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").value().orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getScientists().get(0).getName(), detectionsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").value().orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getSponsors().get(0).getName(), detectionsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").value().orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getFunders().get(0).getName(), detectionsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").value().orElseThrow(), detectionsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").value().orElseThrow(), detectionsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").value().orElseThrow(), detectionsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").value().orElseThrow(), detectionsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").value().orElseThrow(), detectionsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").value().orElseThrow(), detectionsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").value().orElseThrow(), detectionsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").value().orElseThrow(), detectionsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").value().orElseThrow(), detectionsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").value().orElseThrow(), detectionsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").value().orElseThrow(), detectionsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").value().orElseThrow(), detectionsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").value().orElseThrow(), detectionsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").value().orElseThrow(), detectionsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").value().orElseThrow(), detectionsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").value().orElseThrow(), detectionsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").value().orElseThrow(), detectionsPackingJob.getSoftwareProcessingDescription());
    assertEquals(values.get("soundSource").value().orElseThrow(), detectionsPackingJob.getSoundSource().getSource());
    assertEquals(values.get("analysisTimeZone").value().orElseThrow(), detectionsPackingJob.getAnalysisTimeZone().toString());
    assertEquals(values.get("analysisEffort").value().orElseThrow(), detectionsPackingJob.getAnalysisEffort().toString());
    assertEquals(values.get("sampleRate").value().orElseThrow(), detectionsPackingJob.getSampleRate().toString());
    assertEquals(values.get("minFrequency").value().orElseThrow(), detectionsPackingJob.getMinFrequency().toString());
    assertEquals(values.get("maxFrequency").value().orElseThrow(), detectionsPackingJob.getMaxFrequency().toString());
    assertEquals(values.get("qualityAnalyst").value().orElseThrow(), detectionsPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").value().orElseThrow(), detectionsPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").value().orElseThrow(), detectionsPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").value().orElseThrow(), detectionsPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").value().orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getComments());
  }

  @Test
  void testTranslateSoundLevelMetrics() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.SOUND_LEVEL_METRICS, LocationType.STATIONARY_MARINE, new HashMap<>(0));
    values.put("temperaturePath", new ValueWithColumnNumber(Optional.of("temperaturePath"), 1));
    values.put("documentsPath", new ValueWithColumnNumber(Optional.of("documentsPath"), 1));
    values.put("otherPath", new ValueWithColumnNumber(Optional.of("otherPath"), 1));
    values.put("navigationPath", new ValueWithColumnNumber(Optional.of("navigationPath"), 1));
    values.put("calibrationDocumentsPath", new ValueWithColumnNumber(Optional.of("calibrationDocumentsPath"), 1));
    values.put("sourcePath", new ValueWithColumnNumber(Optional.of("sourcePath"), 1));
    values.put("biologicalPath", new ValueWithColumnNumber(Optional.of("biologicalPath"), 1));
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("sound level metrics"), 1));
    values.put("siteOrCruiseName", new ValueWithColumnNumber(Optional.of("site-or-cruise-name"), 1));
    values.put("deploymentId", new ValueWithColumnNumber(Optional.of("deployment-id"), 1));
    values.put("datasetPackager", new ValueWithColumnNumber(Optional.of("dataset-packager"), 1));
    values.put("projects", new ValueWithColumnNumber(Optional.of("project-1;project-2"), 1));
    values.put("publicReleaseDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("scientists", new ValueWithColumnNumber(Optional.of("scientist-1;scientist-2"), 1));
    values.put("sponsors", new ValueWithColumnNumber(Optional.of("sponsor-1;sponsor-2"), 1));
    values.put("funders", new ValueWithColumnNumber(Optional.of("funder-1;funder-2"), 1));
    values.put("platform", new ValueWithColumnNumber(Optional.of("platform"), 1));
    values.put("instrument", new ValueWithColumnNumber(Optional.of("instrument"), 1));
    values.put("startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 1));
    values.put("endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 1));
    values.put("preDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().minusDays(14).toString()), 1));
    values.put("postDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("calibrationDescription", new ValueWithColumnNumber(Optional.of("calibration-description"), 1));
    values.put("deploymentTitle", new ValueWithColumnNumber(Optional.of("deployment-title"), 1));
    values.put("deploymentPurpose", new ValueWithColumnNumber(Optional.of("deployment-purpose"), 1));
    values.put("deploymentDescription", new ValueWithColumnNumber(Optional.of("deployment-description"), 1));
    values.put("alternateSiteName", new ValueWithColumnNumber(Optional.of("alternate-site-name"), 1));
    values.put("alternateDeploymentName", new ValueWithColumnNumber(Optional.of("alternate-deployment-name"), 1));
    values.put("softwareNames", new ValueWithColumnNumber(Optional.of("software-names"), 1));
    values.put("softwareVersions", new ValueWithColumnNumber(Optional.of("software-versions"), 1));
    values.put("softwareProtocolCitation", new ValueWithColumnNumber(Optional.of("software-protocol-citation"), 1));
    values.put("softwareDescription", new ValueWithColumnNumber(Optional.of("software-description"), 1));
    values.put("softwareProcessingDescription", new ValueWithColumnNumber(Optional.of("software-processing-description"), 1));
    values.put("qualityAnalyst", new ValueWithColumnNumber(Optional.of("quality-analyst"), 1));
    values.put("qualityAnalysisObjectives", new ValueWithColumnNumber(Optional.of("quality-analysis-objectives"), 1));
    values.put("qualityAnalysisMethod", new ValueWithColumnNumber(Optional.of("quality-analysis-method"), 1));
    values.put("qualityAssessmentDescription", new ValueWithColumnNumber(Optional.of("quality-assessment-description"), 1));
    values.put("qualityEntries[0].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 1));
    values.put("qualityEntries[0].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 1));
    values.put("qualityEntries[0].minFrequency", new ValueWithColumnNumber(Optional.of("100.0"), 1));
    values.put("qualityEntries[0].maxFrequency", new ValueWithColumnNumber(Optional.of("200.0"), 1));
    values.put("qualityEntries[0].qualityLevel", new ValueWithColumnNumber(Optional.of("Good"), 1));
    values.put("qualityEntries[0].comments", new ValueWithColumnNumber(Optional.of("quality-comment-1"), 1));
    values.put("qualityEntries[1].startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(2).toString()), 1));
    values.put("qualityEntries[1].endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().plusDays(1).toString()), 1));
    values.put("qualityEntries[1].minFrequency", new ValueWithColumnNumber(Optional.of("300.0"), 1));
    values.put("qualityEntries[1].maxFrequency", new ValueWithColumnNumber(Optional.of("400.0"), 1));
    values.put("qualityEntries[1].qualityLevel", new ValueWithColumnNumber(Optional.of("Unusable"), 1));
    values.put("qualityEntries[1].comments", new ValueWithColumnNumber(Optional.of("quality-comment-2"), 1));
    values.put("analysisTimeZone", new ValueWithColumnNumber(Optional.of("1"), 1));
    values.put("analysisEffort", new ValueWithColumnNumber(Optional.of("2"), 1));
    values.put("sampleRate", new ValueWithColumnNumber(Optional.of("3.0"), 1));
    values.put("minFrequency", new ValueWithColumnNumber(Optional.of("4.0"), 1));
    values.put("maxFrequency", new ValueWithColumnNumber(Optional.of("5.0"), 1));
    values.put("audioStartTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 1));
    values.put("audioEndTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 1));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(Person.builder()
        .name("dataset-packager")
        .build());
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(Person.builder()
        .name("scientist-1")
        .build());
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(Person.builder()
        .name("scientist-2")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-1")).thenReturn(Organization.builder()
        .name("sponsor-1")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-2")).thenReturn(Organization.builder()
        .name("sponsor-2")
        .build());
    when(organizationRepository.getByUniqueField("funder-1")).thenReturn(Organization.builder()
        .name("funder-1")
        .build());
    when(organizationRepository.getByUniqueField("funder-2")).thenReturn(Organization.builder()
        .name("funder-2")
        .build());
    when(projectRepository.getByUniqueField("project-1")).thenReturn(Project.builder()
        .name("project-1")
        .build());
    when(projectRepository.getByUniqueField("project-2")).thenReturn(Project.builder()
        .name("project-2")
        .build());
    when(platformRepository.getByUniqueField("platform")).thenReturn(Platform.builder()
        .name("platform")
        .build());
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(Instrument.builder()
        .name("instrument")
        .build());
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(Person.builder()
        .name("quality-analyst")
        .build());
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(DepthSensor.builder()
        .name("sensor-1")
        .build());
    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(AudioSensor.builder()
        .name("sensor-2")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(SoundLevelMetricsPackage.class, packingJob);

    SoundLevelMetricsPackage soundLevelMetricsPackingJob = (SoundLevelMetricsPackage) packingJob;
    assertEquals(values.get("temperaturePath").value().orElseThrow(), soundLevelMetricsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").value().orElseThrow(), soundLevelMetricsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").value().orElseThrow(), soundLevelMetricsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").value().orElseThrow(), soundLevelMetricsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").value().orElseThrow(), soundLevelMetricsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").value().orElseThrow(), soundLevelMetricsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").value().orElseThrow(), soundLevelMetricsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").value().orElseThrow(), soundLevelMetricsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").value().orElseThrow(), soundLevelMetricsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").value().orElseThrow(), soundLevelMetricsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").value().orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getProjects().get(0).getName(), soundLevelMetricsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").value().orElseThrow(), soundLevelMetricsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").value().orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getScientists().get(0).getName(), soundLevelMetricsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").value().orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getSponsors().get(0).getName(), soundLevelMetricsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").value().orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getFunders().get(0).getName(), soundLevelMetricsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").value().orElseThrow(), soundLevelMetricsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").value().orElseThrow(), soundLevelMetricsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").value().orElseThrow(), soundLevelMetricsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").value().orElseThrow(), soundLevelMetricsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").value().orElseThrow(), soundLevelMetricsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").value().orElseThrow(), soundLevelMetricsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").value().orElseThrow(), soundLevelMetricsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").value().orElseThrow(), soundLevelMetricsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").value().orElseThrow(), soundLevelMetricsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").value().orElseThrow(), soundLevelMetricsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").value().orElseThrow(), soundLevelMetricsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").value().orElseThrow(), soundLevelMetricsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").value().orElseThrow(), soundLevelMetricsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").value().orElseThrow(), soundLevelMetricsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").value().orElseThrow(), soundLevelMetricsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").value().orElseThrow(), soundLevelMetricsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").value().orElseThrow(), soundLevelMetricsPackingJob.getSoftwareProcessingDescription());
    assertEquals(values.get("analysisTimeZone").value().orElseThrow(), soundLevelMetricsPackingJob.getAnalysisTimeZone().toString());
    assertEquals(values.get("analysisEffort").value().orElseThrow(), soundLevelMetricsPackingJob.getAnalysisEffort().toString());
    assertEquals(values.get("sampleRate").value().orElseThrow(), soundLevelMetricsPackingJob.getSampleRate().toString());
    assertEquals(values.get("minFrequency").value().orElseThrow(), soundLevelMetricsPackingJob.getMinFrequency().toString());
    assertEquals(values.get("maxFrequency").value().orElseThrow(), soundLevelMetricsPackingJob.getMaxFrequency().toString());
    assertEquals(values.get("audioStartTime").value().orElseThrow(), soundLevelMetricsPackingJob.getAudioStartTime().toString());
    assertEquals(values.get("audioEndTime").value().orElseThrow(), soundLevelMetricsPackingJob.getAudioEndTime().toString());
    assertEquals(values.get("qualityAnalyst").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").value().orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getComments());
  }

  @Test
  void testTranslateSoundPropagationModels() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.SOUND_PROPAGATION_MODELS, LocationType.STATIONARY_MARINE, new HashMap<>(0));
    values.put("temperaturePath", new ValueWithColumnNumber(Optional.of("temperaturePath"), 1));
    values.put("documentsPath", new ValueWithColumnNumber(Optional.of("documentsPath"), 1));
    values.put("otherPath", new ValueWithColumnNumber(Optional.of("otherPath"), 1));
    values.put("navigationPath", new ValueWithColumnNumber(Optional.of("navigationPath"), 1));
    values.put("calibrationDocumentsPath", new ValueWithColumnNumber(Optional.of("calibrationDocumentsPath"), 1));
    values.put("sourcePath", new ValueWithColumnNumber(Optional.of("sourcePath"), 1));
    values.put("biologicalPath", new ValueWithColumnNumber(Optional.of("biologicalPath"), 1));
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("sound propagation models"), 1));
    values.put("siteOrCruiseName", new ValueWithColumnNumber(Optional.of("site-or-cruise-name"), 1));
    values.put("deploymentId", new ValueWithColumnNumber(Optional.of("deployment-id"), 1));
    values.put("datasetPackager", new ValueWithColumnNumber(Optional.of("dataset-packager"), 1));
    values.put("projects", new ValueWithColumnNumber(Optional.of("project-1;project-2"), 1));
    values.put("publicReleaseDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("scientists", new ValueWithColumnNumber(Optional.of("scientist-1;scientist-2"), 1));
    values.put("sponsors", new ValueWithColumnNumber(Optional.of("sponsor-1;sponsor-2"), 1));
    values.put("funders", new ValueWithColumnNumber(Optional.of("funder-1;funder-2"), 1));
    values.put("platform", new ValueWithColumnNumber(Optional.of("platform"), 1));
    values.put("instrument", new ValueWithColumnNumber(Optional.of("instrument"), 1));
    values.put("startTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 1));
    values.put("endTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().toString()), 1));
    values.put("preDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().minusDays(14).toString()), 1));
    values.put("postDeploymentCalibrationDate", new ValueWithColumnNumber(Optional.of(LocalDate.now().toString()), 1));
    values.put("calibrationDescription", new ValueWithColumnNumber(Optional.of("calibration-description"), 1));
    values.put("deploymentTitle", new ValueWithColumnNumber(Optional.of("deployment-title"), 1));
    values.put("deploymentPurpose", new ValueWithColumnNumber(Optional.of("deployment-purpose"), 1));
    values.put("deploymentDescription", new ValueWithColumnNumber(Optional.of("deployment-description"), 1));
    values.put("alternateSiteName", new ValueWithColumnNumber(Optional.of("alternate-site-name"), 1));
    values.put("alternateDeploymentName", new ValueWithColumnNumber(Optional.of("alternate-deployment-name"), 1));
    values.put("softwareNames", new ValueWithColumnNumber(Optional.of("software-names"), 1));
    values.put("softwareVersions", new ValueWithColumnNumber(Optional.of("software-versions"), 1));
    values.put("softwareProtocolCitation", new ValueWithColumnNumber(Optional.of("software-protocol-citation"), 1));
    values.put("softwareDescription", new ValueWithColumnNumber(Optional.of("software-description"), 1));
    values.put("softwareProcessingDescription", new ValueWithColumnNumber(Optional.of("software-processing-description"), 1));
    values.put("audioStartTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(14).toString()), 1));
    values.put("audioEndTime", new ValueWithColumnNumber(Optional.of(LocalDateTime.now().minusDays(1).toString()), 1));
    values.put("modeledFrequency", new ValueWithColumnNumber(Optional.of("1.0"), 1));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(personRepository.getByUniqueField("dataset-packager")).thenReturn(Person.builder()
        .name("dataset-packager")
        .build());
    when(personRepository.getByUniqueField("scientist-1")).thenReturn(Person.builder()
        .name("scientist-1")
        .build());
    when(personRepository.getByUniqueField("scientist-2")).thenReturn(Person.builder()
        .name("scientist-2")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-1")).thenReturn(Organization.builder()
        .name("sponsor-1")
        .build());
    when(organizationRepository.getByUniqueField("sponsor-2")).thenReturn(Organization.builder()
        .name("sponsor-2")
        .build());
    when(organizationRepository.getByUniqueField("funder-1")).thenReturn(Organization.builder()
        .name("funder-1")
        .build());
    when(organizationRepository.getByUniqueField("funder-2")).thenReturn(Organization.builder()
        .name("funder-2")
        .build());
    when(projectRepository.getByUniqueField("project-1")).thenReturn(Project.builder()
        .name("project-1")
        .build());
    when(projectRepository.getByUniqueField("project-2")).thenReturn(Project.builder()
        .name("project-2")
        .build());
    when(platformRepository.getByUniqueField("platform")).thenReturn(Platform.builder()
        .name("platform")
        .build());
    when(instrumentRepository.getByUniqueField("instrument")).thenReturn(Instrument.builder()
        .name("instrument")
        .build());
    when(personRepository.getByUniqueField("quality-analyst")).thenReturn(Person.builder()
        .name("quality-analyst")
        .build());
    when(sensorRepository.getByUniqueField("sensor-1")).thenReturn(DepthSensor.builder()
        .name("sensor-1")
        .build());
    when(sensorRepository.getByUniqueField("sensor-2")).thenReturn(AudioSensor.builder()
        .name("sensor-2")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(SoundPropagationModelsPackage.class, packingJob);

    SoundPropagationModelsPackage soundPropagationModelsPackingJob = (SoundPropagationModelsPackage) packingJob;
    assertEquals(values.get("temperaturePath").value().orElseThrow(), soundPropagationModelsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").value().orElseThrow(), soundPropagationModelsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").value().orElseThrow(), soundPropagationModelsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").value().orElseThrow(), soundPropagationModelsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").value().orElseThrow(), soundPropagationModelsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").value().orElseThrow(), soundPropagationModelsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").value().orElseThrow(), soundPropagationModelsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").value().orElseThrow(), soundPropagationModelsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").value().orElseThrow(), soundPropagationModelsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").value().orElseThrow(), soundPropagationModelsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").value().orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getProjects().get(0).getName(), soundPropagationModelsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").value().orElseThrow(), soundPropagationModelsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").value().orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getScientists().get(0).getName(), soundPropagationModelsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").value().orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getSponsors().get(0).getName(), soundPropagationModelsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").value().orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getFunders().get(0).getName(), soundPropagationModelsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").value().orElseThrow(), soundPropagationModelsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").value().orElseThrow(), soundPropagationModelsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").value().orElseThrow(), soundPropagationModelsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").value().orElseThrow(), soundPropagationModelsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").value().orElseThrow(), soundPropagationModelsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").value().orElseThrow(), soundPropagationModelsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").value().orElseThrow(), soundPropagationModelsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").value().orElseThrow(), soundPropagationModelsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").value().orElseThrow(), soundPropagationModelsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").value().orElseThrow(), soundPropagationModelsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").value().orElseThrow(), soundPropagationModelsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").value().orElseThrow(), soundPropagationModelsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").value().orElseThrow(), soundPropagationModelsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").value().orElseThrow(), soundPropagationModelsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").value().orElseThrow(), soundPropagationModelsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").value().orElseThrow(), soundPropagationModelsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").value().orElseThrow(), soundPropagationModelsPackingJob.getSoftwareProcessingDescription());
    assertEquals(values.get("audioStartTime").value().orElseThrow(), soundPropagationModelsPackingJob.getAudioStartTime().toString());
    assertEquals(values.get("audioEndTime").value().orElseThrow(), soundPropagationModelsPackingJob.getAudioEndTime().toString());
  }
  
  @Test
  void testInvalidDatasetDateTime() {
    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.AUDIO, LocationType.MOBILE_MARINE, Map.of(
        "datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1),
        "startTime", new ValueWithColumnNumber(Optional.of("TEST"), 1)
    ));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);
    
    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(values, Package.class, 0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    ));
    assertEquals(0, exception.getRow());
    assertEquals("Translation failed", exception.getMessage());
    
    assertEquals(1, exception.getSuppressed().length);
    assertInstanceOf(FieldException.class, exception.getSuppressed()[0]);
    FieldException fieldException = (FieldException) exception.getSuppressed()[0];
    assertEquals("startTime", fieldException.getProperty());
    assertEquals("invalid date time format", fieldException.getMessage());
  }

  @Test
  void testInvalidDatasetDate() {
    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.AUDIO, LocationType.STATIONARY_MARINE, Map.of(
        "datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1),
        "publicReleaseDate", new ValueWithColumnNumber(Optional.of("TEST"), 1)
    ));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(values, Package.class, 0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    ));
    assertEquals(0, exception.getRow());
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    assertInstanceOf(FieldException.class, exception.getSuppressed()[0]);
    FieldException fieldException = (FieldException) exception.getSuppressed()[0];
    assertEquals("publicReleaseDate", fieldException.getProperty());
    assertEquals("invalid date format", fieldException.getMessage());
  }

  @Test
  void testInvalidDatasetInteger() {
    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.DETECTIONS, LocationType.STATIONARY_MARINE, Map.of(
        "datasetType", new ValueWithColumnNumber(Optional.of("detections"), 1),
        "analysisEffort", new ValueWithColumnNumber(Optional.of("TEST"), 1)
    ));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(values, Package.class, 0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    ));
    assertEquals(0, exception.getRow());
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    assertInstanceOf(FieldException.class, exception.getSuppressed()[0]);
    FieldException fieldException = (FieldException) exception.getSuppressed()[0];
    assertEquals("analysisEffort", fieldException.getProperty());
    assertEquals("invalid integer format", fieldException.getMessage());
  }

  @Test
  void testInvalidDatasetQualityLevel() {
    Map<String, ValueWithColumnNumber> values = createDatasetMap(DatasetType.AUDIO, LocationType.MOBILE_MARINE, Map.of(
        "datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1),
        "qualityEntries[0].qualityLevel", new ValueWithColumnNumber(Optional.of("TEST"), 1)
    ));

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(values, Package.class, 0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    ));
    assertEquals(0, exception.getRow());
    assertEquals("Translation failed", exception.getMessage());

    assertEquals(1, exception.getSuppressed().length);
    assertInstanceOf(FieldException.class, exception.getSuppressed()[0]);
    FieldException fieldException = (FieldException) exception.getSuppressed()[0];
    assertEquals("qualityEntries[0].qualityLevel", fieldException.getProperty());
    assertEquals("Invalid quality level. Was not one of Unverified, Good, Compromised, Unusable", fieldException.getMessage());
  }

  @Test
  void testTranslateStationaryMarineLocation() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1));
    values.put("locationType", new ValueWithColumnNumber(Optional.of("stationary marine"), 1));
    values.put("seaArea", new ValueWithColumnNumber(Optional.of("sea-area"), 1));
    values.put("deploymentLocation.latitude", new ValueWithColumnNumber(Optional.of("1.0"), 1));
    values.put("recoveryLocation.latitude", new ValueWithColumnNumber(Optional.of("5.0"), 1));
    values.put("deploymentLocation.longitude", new ValueWithColumnNumber(Optional.of("2.0"), 1));
    values.put("recoveryLocation.longitude", new ValueWithColumnNumber(Optional.of("6.0"), 1));
    values.put("deploymentLocation.seaFloorDepth", new ValueWithColumnNumber(Optional.of("3.0"), 1));
    values.put("recoveryLocation.seaFloorDepth", new ValueWithColumnNumber(Optional.of("7.0"), 1));
    values.put("deploymentLocation.instrumentDepth", new ValueWithColumnNumber(Optional.of("4.0"), 1));
    values.put("recoveryLocation.instrumentDepth", new ValueWithColumnNumber(Optional.of("8.0"), 1));
    
    values = createDatasetMap(DatasetType.AUDIO, LocationType.STATIONARY_MARINE, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);
    
    when(seaRepository.getByUniqueField("sea-area")).thenReturn(Sea.builder()
            .name("sea-area")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(AudioPackage.class, packingJob);

    AudioPackage audioPackingJob = (AudioPackage) packingJob;
    LocationDetail locationDetail = audioPackingJob.getLocationDetail();
    assertInstanceOf(StationaryMarineLocation.class, locationDetail);
    StationaryMarineLocation stationaryMarineLocation = (StationaryMarineLocation) locationDetail;
    assertEquals(values.get("seaArea").value().orElseThrow(), stationaryMarineLocation.getSeaArea().getName());
    assertEquals(values.get("deploymentLocation.latitude").value().orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getLatitude().toString());
    assertEquals(values.get("recoveryLocation.latitude").value().orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getLatitude().toString());
    assertEquals(values.get("deploymentLocation.longitude").value().orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getLongitude().toString());
    assertEquals(values.get("recoveryLocation.longitude").value().orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getLongitude().toString());
    assertEquals(values.get("deploymentLocation.seaFloorDepth").value().orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getSeaFloorDepth().toString());
    assertEquals(values.get("recoveryLocation.seaFloorDepth").value().orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getSeaFloorDepth().toString());
    assertEquals(values.get("deploymentLocation.instrumentDepth").value().orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getInstrumentDepth().toString());
    assertEquals(values.get("recoveryLocation.instrumentDepth").value().orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getInstrumentDepth().toString());
  }

  @Test
  void testTranslateMultiPointStationaryMarineLocation() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1));
    values.put("locationType", new ValueWithColumnNumber(Optional.of("multipoint stationary marine"), 1));
    values.put("seaArea", new ValueWithColumnNumber(Optional.of("sea-area"), 1));
    values.put("locations[0].latitude", new ValueWithColumnNumber(Optional.of("1.0"), 1));
    values.put("locations[0].longitude", new ValueWithColumnNumber(Optional.of("2.0"), 1));
    values.put("locations[0].seaFloorDepth", new ValueWithColumnNumber(Optional.of("3.0"), 1));
    values.put("locations[0].instrumentDepth", new ValueWithColumnNumber(Optional.of("4.0"), 1));
    values.put("locations[1].latitude", new ValueWithColumnNumber(Optional.of("5.0"), 1));
    values.put("locations[1].longitude", new ValueWithColumnNumber(Optional.of("6.0"), 1));
    values.put("locations[1].seaFloorDepth", new ValueWithColumnNumber(Optional.of("7.0"), 1));
    values.put("locations[1].instrumentDepth", new ValueWithColumnNumber(Optional.of("8.0"), 1));
    
    values = createDatasetMap(DatasetType.AUDIO, LocationType.MULTIPOINT_STATIONARY_MARINE, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(seaRepository.getByUniqueField("sea-area")).thenReturn(Sea.builder()
        .name("sea-area")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(AudioPackage.class, packingJob);

    AudioPackage audioPackingJob = (AudioPackage) packingJob;
    LocationDetail locationDetail = audioPackingJob.getLocationDetail();
    assertInstanceOf(MultiPointStationaryMarineLocation.class, locationDetail);
    MultiPointStationaryMarineLocation multiPointStationaryMarineLocation = (MultiPointStationaryMarineLocation) locationDetail;
    assertEquals(values.get("seaArea").value().orElseThrow(), multiPointStationaryMarineLocation.getSeaArea().getName());
    assertEquals(values.get("locations[0].latitude").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getLatitude().toString());
    assertEquals(values.get("locations[1].latitude").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getLatitude().toString());
    assertEquals(values.get("locations[0].longitude").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getLongitude().toString());
    assertEquals(values.get("locations[1].longitude").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getLongitude().toString());
    assertEquals(values.get("locations[0].seaFloorDepth").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getSeaFloorDepth().toString());
    assertEquals(values.get("locations[1].seaFloorDepth").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getSeaFloorDepth().toString());
    assertEquals(values.get("locations[0].instrumentDepth").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getInstrumentDepth().toString());
    assertEquals(values.get("locations[1].instrumentDepth").value().orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getInstrumentDepth().toString());
  }

  @Test
  void testTranslateMobileMarineLocation() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1));
    values.put("locationType", new ValueWithColumnNumber(Optional.of("mobile marine"), 1));
    values.put("type", new ValueWithColumnNumber(Optional.of("mobile marine"), 1));
    values.put("seaArea", new ValueWithColumnNumber(Optional.of("sea-area"), 1));
    values.put("vessel", new ValueWithColumnNumber(Optional.of("vessel"), 1));
    values.put("locationDerivationDescription", new ValueWithColumnNumber(Optional.of("location-derivation-description"), 1));
    
    values = createDatasetMap(DatasetType.AUDIO, LocationType.MOBILE_MARINE, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    when(seaRepository.getByUniqueField("sea-area")).thenReturn(Sea.builder()
        .name("sea-area")
        .build());
    when(shipRepository.getByUniqueField("vessel")).thenReturn(Ship.builder()
            .name("vessel")
        .build());

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(AudioPackage.class, packingJob);

    AudioPackage audioPackingJob = (AudioPackage) packingJob;
    LocationDetail locationDetail = audioPackingJob.getLocationDetail();
    assertInstanceOf(MobileMarineLocation.class, locationDetail);
    MobileMarineLocation mobileMarineLocation = (MobileMarineLocation) locationDetail;
    assertEquals(values.get("seaArea").value().orElseThrow(), mobileMarineLocation.getSeaArea().getName());
    assertEquals(values.get("vessel").value().orElseThrow(), mobileMarineLocation.getVessel().getName());
    assertEquals(values.get("locationDerivationDescription").value().orElseThrow(), mobileMarineLocation.getLocationDerivationDescription());
  }

  @Test
  void testTranslateStationaryTerrestrialLocation() throws RowConversionException {

    Map<String, ValueWithColumnNumber> values = new HashMap<>(0);
    values.put("datasetType", new ValueWithColumnNumber(Optional.of("audio"), 1));
    values.put("locationType", new ValueWithColumnNumber(Optional.of("stationary terrestrial"), 1));
    values.put("latitude", new ValueWithColumnNumber(Optional.of("1.0"), 1));
    values.put("longitude", new ValueWithColumnNumber(Optional.of("2.0"), 1));
    values.put("surfaceElevation", new ValueWithColumnNumber(Optional.of("3.0"), 1));
    values.put("instrumentElevation", new ValueWithColumnNumber(Optional.of("4.0"), 1));
    
    values = createDatasetMap(DatasetType.AUDIO, LocationType.STATIONARY_TERRESTRIAL, values);

    ProjectRepository projectRepository = mock(ProjectRepository.class);
    PersonRepository personRepository = mock(PersonRepository.class);
    OrganizationRepository organizationRepository = mock(OrganizationRepository.class);
    PlatformRepository platformRepository = mock(PlatformRepository.class);
    InstrumentRepository instrumentRepository = mock(InstrumentRepository.class);
    SensorRepository sensorRepository = mock(SensorRepository.class);
    DetectionTypeRepository detectionTypeRepository = mock(DetectionTypeRepository.class);
    SeaRepository seaRepository = mock(SeaRepository.class);
    ShipRepository shipRepository = mock(ShipRepository.class);

    Package packingJob = TranslatorUtils.convertMapToObject(
        values,
        Package.class,
        0,
        projectRepository, personRepository, organizationRepository, platformRepository, instrumentRepository,
        sensorRepository, detectionTypeRepository, seaRepository, shipRepository
    );

    assertInstanceOf(AudioPackage.class, packingJob);

    AudioPackage audioPackingJob = (AudioPackage) packingJob;
    LocationDetail locationDetail = audioPackingJob.getLocationDetail();
    assertInstanceOf(StationaryTerrestrialLocation.class, locationDetail);
    StationaryTerrestrialLocation stationaryTerrestrialLocation = (StationaryTerrestrialLocation) locationDetail;
    assertEquals(values.get("latitude").value().orElseThrow(), stationaryTerrestrialLocation.getLatitude().toString());
    assertEquals(values.get("longitude").value().orElseThrow(), stationaryTerrestrialLocation.getLongitude().toString());
    assertEquals(values.get("surfaceElevation").value().orElseThrow(), stationaryTerrestrialLocation.getSurfaceElevation().toString());
    assertEquals(values.get("instrumentElevation").value().orElseThrow(), stationaryTerrestrialLocation.getInstrumentElevation().toString());
  }
  
  private Map<String, ValueWithColumnNumber> createDatasetMap(DatasetType datasetType, LocationType locationType, Map<String, ValueWithColumnNumber> additionalProperties) {
    List<String> propertyNames = FieldNameFactory.getDatasetDeclaredFields(datasetType, locationType);
    Map<String, ValueWithColumnNumber> values = IntStream.range(0, propertyNames.size()).boxed()
        .collect(Collectors.toMap(
            propertyNames::get,
            i -> {
              Optional<String> value;
              if (propertyNames.get(i).equals("datasetType")) {
                value = Optional.of(datasetType.getName());
              } else if (propertyNames.get(i).equals("locationType")) {
                value = Optional.of(locationType.getName());
              } else {
                value = Optional.of("");
              }
              
              return new ValueWithColumnNumber(
                  value,
                  i
              );
            }
        ));
    values.putAll(additionalProperties);
    return values;
  }

}
