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
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
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
import edu.colorado.cires.pace.translator.TranslatorExecutorTest.TestTranslator;
import edu.colorado.cires.pace.translator.TranslatorExecutorTest.TestTranslatorField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TranslatorUtilsTest {
  
  @Test
  void testValidatePersonTranslator() {
    assertDoesNotThrow(() -> TranslatorUtils.validateTranslator(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 0),
        new TestTranslatorField("name", 1),
        new TestTranslatorField("organization", 2),
        new TestTranslatorField("position", 3),
        new TestTranslatorField("street", 4),
        new TestTranslatorField("city", 5),
        new TestTranslatorField("state", 6),
        new TestTranslatorField("zip", 7),
        new TestTranslatorField("country", 8),
        new TestTranslatorField("email", 9),
        new TestTranslatorField("phone", 10),
        new TestTranslatorField("orcid", 11)
    )), Person.class));
    
    Exception exception = assertThrows(TranslatorValidationException.class, () -> TranslatorUtils.validateTranslator(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 0),
        new TestTranslatorField("name", 1),
        new TestTranslatorField("position", 3),
        new TestTranslatorField("street", 4),
        new TestTranslatorField("city", 5),
        new TestTranslatorField("state", 6),
        new TestTranslatorField("zip", 7),
        new TestTranslatorField("country", 8),
        new TestTranslatorField("email", 9),
        new TestTranslatorField("phone", 10),
        new TestTranslatorField("orcid", 11)
    )), Person.class));
    assertEquals("Translator does not fully describe Person. Missing fields: [organization]", exception.getMessage());
  }

  @Test
  void testValidateOrganizationTranslator() {
    assertDoesNotThrow(() -> TranslatorUtils.validateTranslator(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 0),
        new TestTranslatorField("name", 1),
        new TestTranslatorField("street", 4),
        new TestTranslatorField("city", 5),
        new TestTranslatorField("state", 6),
        new TestTranslatorField("zip", 7),
        new TestTranslatorField("country", 8),
        new TestTranslatorField("email", 9),
        new TestTranslatorField("phone", 10)
    )), Organization.class));

    Exception exception = assertThrows(TranslatorValidationException.class, () -> TranslatorUtils.validateTranslator(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 0),
        new TestTranslatorField("name", 1),
        new TestTranslatorField("city", 5),
        new TestTranslatorField("state", 6),
        new TestTranslatorField("zip", 7),
        new TestTranslatorField("country", 8),
        new TestTranslatorField("email", 9),
        new TestTranslatorField("phone", 10)
    )), Organization.class));
    assertEquals("Translator does not fully describe Organization. Missing fields: [street]", exception.getMessage());
  }
  
  @Test
  void testConvertPerson() throws RowConversionException {
    Map<String, Optional<String>> map = new HashMap<>(0);
    map.put("uuid", Optional.of(UUID.randomUUID().toString()));
    map.put("name", Optional.of("person-name"));
    map.put("organization", Optional.of("person-organization"));
    map.put("position", Optional.of("person-position"));
    map.put("street", Optional.of("person-street"));
    map.put("city", Optional.of("person-city"));
    map.put("state", Optional.of("person-state"));
    map.put("zip", Optional.of("person-zip"));
    map.put("country", Optional.of("person-country"));
    map.put("email", Optional.of("person-email"));
    map.put("phone", Optional.of("person-phone"));
    map.put("orcid", Optional.of("person-orcid"));
    
    Person person = TranslatorUtils.convertMapToObject(map, Person.class, 0);
    assertEquals(map.get("uuid").orElseThrow(), person.getUuid().toString());
    assertEquals(map.get("name").orElseThrow(), person.getName());
    assertEquals(map.get("organization").orElseThrow(), person.getOrganization());
    assertEquals(map.get("position").orElseThrow(), person.getPosition());
    assertEquals(map.get("street").orElseThrow(), person.getStreet());
    assertEquals(map.get("city").orElseThrow(), person.getCity());
    assertEquals(map.get("state").orElseThrow(), person.getState());
    assertEquals(map.get("zip").orElseThrow(), person.getZip());
    assertEquals(map.get("country").orElseThrow(), person.getCountry());
    assertEquals(map.get("email").orElseThrow(), person.getEmail());
    assertEquals(map.get("phone").orElseThrow(), person.getPhone());
    assertEquals(map.get("orcid").orElseThrow(), person.getOrcid());
  }
  
  @Test
  void testConvertOrganization() throws RowConversionException {
    Map<String, Optional<String>> map = new HashMap<>(0);
    map.put("uuid", Optional.of(UUID.randomUUID().toString()));
    map.put("name", Optional.of("organization-name"));
    map.put("street", Optional.of("organization-street"));
    map.put("city", Optional.of("organization-city"));
    map.put("state", Optional.of("organization-state"));
    map.put("zip", Optional.of("organization-zip"));
    map.put("country", Optional.of("organization-country"));
    map.put("email", Optional.of("organization-email"));
    map.put("phone", Optional.of("organization-phone"));

    Organization organization = TranslatorUtils.convertMapToObject(map, Organization.class, 0);
    assertEquals(map.get("uuid").orElseThrow(), organization.getUuid().toString());
    assertEquals(map.get("name").orElseThrow(), organization.getName());
    assertEquals(map.get("street").orElseThrow(), organization.getStreet());
    assertEquals(map.get("city").orElseThrow(), organization.getCity());
    assertEquals(map.get("state").orElseThrow(), organization.getState());
    assertEquals(map.get("zip").orElseThrow(), organization.getZip());
    assertEquals(map.get("country").orElseThrow(), organization.getCountry());
    assertEquals(map.get("email").orElseThrow(), organization.getEmail());
    assertEquals(map.get("phone").orElseThrow(), organization.getPhone());
  }
  
  @Test
  void testUnsupportedType() {
    record TestObject(String name) {}
    
    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "p1", Optional.of("2")
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name")
    );
    
    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz, 1);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }
  
  @Test
  void testConvertFileType() throws RowConversionException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "type", Optional.of("test-type"),
        "comment", Optional.of("test-comment")
    );

    FileType object = TranslatorUtils.convertMapToObject(propertyMap, FileType.class, 1);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("type").orElseThrow(
        () -> new IllegalStateException("type not found in propertyMap")
    ), object.getType());
    assertEquals(propertyMap.get("comment").orElseThrow(
        () -> new IllegalStateException("comment not found in propertyMap")
    ), object.getComment());
  }

  @Test
  void testConvertFileTypeBadUUID() {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("TEST"),
        "type", Optional.of("test-type"),
        "comment", Optional.of("test-comment")
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "source", Optional.of("test-name"),
        "scienceName", Optional.of("test-scientific-name")
    );

    DetectionType object = TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("source").orElseThrow(
        () -> new IllegalStateException("source not found in propertyMap")
    ), object.getSource());
    assertEquals(propertyMap.get("scienceName").orElseThrow(
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
    Map<String, Optional<String>> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name"),
        "description", Optional.of("test-description"),
        "position.x", Optional.of("1.0"),
        "position.y", Optional.of("2.0"),
        "position.z", Optional.of("3.0"),
        "type", Optional.of(type)
    ));
    
    if (type.equals("other")) {
      propertyMap.put(
          "properties", Optional.of("test-properties")
      );
      propertyMap.put(
          "sensorType", Optional.of("test-type")
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", Optional.of("test-hydrophoneId")
      );
      propertyMap.put(
          "preampId", Optional.of("test-preampId")
      );
    }
    
    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1);
    assertEquals(propertyMap.get("uuid").orElseThrow(), sensor.getUuid().toString());
    assertEquals(propertyMap.get("name").orElseThrow(), sensor.getName());
    assertEquals(propertyMap.get("description").orElseThrow(), sensor.getDescription());
    assertEquals(propertyMap.get("position.x").orElseThrow(), sensor.getPosition().getX().toString());
    assertEquals(propertyMap.get("position.y").orElseThrow(), sensor.getPosition().getY().toString());
    assertEquals(propertyMap.get("position.z").orElseThrow(), sensor.getPosition().getZ().toString());
    
    if (type.equals("other")) {
      assertEquals(propertyMap.get("properties").orElseThrow(), ((OtherSensor) sensor).getProperties());
      assertEquals(propertyMap.get("sensorType").orElseThrow(), ((OtherSensor) sensor).getSensorType());
    } else if (type.equals("audio")) {
      assertEquals(propertyMap.get("hydrophoneId").orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
      assertEquals(propertyMap.get("preampId").orElseThrow(), ((AudioSensor) sensor).getPreampId());
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "name", Optional.of("test-name")
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz, 1);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSourceMissingProperty() throws RowConversionException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "source", Optional.of("test-name")
    );

    DetectionType object = TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("source").orElseThrow(
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
    Map<String, Optional<String>> propertyMap = new java.util.HashMap<>(Map.of(
        "name", Optional.of("test-name"),
        "description", Optional.of("test-description"),
        "position.x", Optional.of("1.0"),
        "position.y", Optional.of("2.0"),
        "position.z", Optional.of("3.0"),
        "type", Optional.of(type)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", Optional.of("test-properties")
      );
      propertyMap.put(
          "sensorType", Optional.of("test-type")
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", Optional.of("test-hydrophoneId")
      );
      propertyMap.put(
          "preampId", Optional.of("test-preampId")
      );
    }

    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1);
    assertNull(sensor.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(), sensor.getName());
    assertEquals(propertyMap.get("description").orElseThrow(), sensor.getDescription());
    assertEquals(propertyMap.get("position.x").orElseThrow(), sensor.getPosition().getX().toString());
    assertEquals(propertyMap.get("position.y").orElseThrow(), sensor.getPosition().getY().toString());
    assertEquals(propertyMap.get("position.z").orElseThrow(), sensor.getPosition().getZ().toString());

    if (type.equals("other")) {
      assertEquals(propertyMap.get("properties").orElseThrow(), ((OtherSensor) sensor).getProperties());
      assertEquals(propertyMap.get("sensorType").orElseThrow(), ((OtherSensor) sensor).getSensorType());
    } else if (type.equals("audio")) {
      assertEquals(propertyMap.get("hydrophoneId").orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
      assertEquals(propertyMap.get("preampId").orElseThrow(), ((AudioSensor) sensor).getPreampId());
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.empty(),
        "name", Optional.of("test-name")
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz, 1);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSourceNullUUID() throws RowConversionException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.empty(),
        "source", Optional.of("test-name")
    );

    DetectionType object = TranslatorUtils.convertMapToObject(propertyMap, DetectionType.class, 1);
    assertNull(object.getUuid());
    assertNull(object.getScienceName());
    assertEquals(propertyMap.get("source").orElseThrow(
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
    Map<String, Optional<String>> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", Optional.empty(),
        "name", Optional.of("test-name"),
        "description", Optional.of("test-description"),
        "position.x", Optional.of("1.0"),
        "position.y", Optional.of("2.0"),
        "position.z", Optional.of("3.0"),
        "type", Optional.of(type)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", Optional.of("test-properties")
      );
      propertyMap.put(
          "sensorType", Optional.of("test-type")
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", Optional.of("test-hydrophoneId")
      );
      propertyMap.put(
          "preampId", Optional.of("test-preampId")
      );
    }

    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class, 1);
    assertNull(sensor.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(), sensor.getName());
    assertEquals(propertyMap.get("description").orElseThrow(), sensor.getDescription());
    assertEquals(propertyMap.get("position.x").orElseThrow(), sensor.getPosition().getX().toString());
    assertEquals(propertyMap.get("position.y").orElseThrow(), sensor.getPosition().getY().toString());
    assertEquals(propertyMap.get("position.z").orElseThrow(), sensor.getPosition().getZ().toString());

    if (type.equals("other")) {
      assertEquals(propertyMap.get("properties").orElseThrow(), ((OtherSensor) sensor).getProperties());
      assertEquals(propertyMap.get("sensorType").orElseThrow(), ((OtherSensor) sensor).getSensorType());
    } else if (type.equals("audio")) {
      assertEquals(propertyMap.get("hydrophoneId").orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
      assertEquals(propertyMap.get("preampId").orElseThrow(), ((AudioSensor) sensor).getPreampId());
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.empty()
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.empty()
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
    Map<String, Optional<String>> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.empty(),
        "description", Optional.of("test-description"),
        "position.x", Optional.of("1.0"),
        "position.y", Optional.of("2.0"),
        "position.z", Optional.of("3.0"),
        "type", Optional.of(type)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", Optional.of("test-properties")
      );
      propertyMap.put(
          "sensorType", Optional.of("test-type")
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", Optional.of("test-hydrophoneId")
      );
      propertyMap.put(
          "preampId", Optional.of("test-preampId")
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.of("test-name")
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
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.of("test-name")
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
    Map<String, Optional<String>> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.of("test-name"),
        "description", Optional.of("test-description"),
        "position.x", Optional.of("1.0"),
        "position.y", Optional.of("2.0"),
        "position.z", Optional.of("3.0"),
        "type", Optional.of(type)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", Optional.of("test-properties")
      );
      propertyMap.put(
          "sensorType", Optional.of("test-type")
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", Optional.of("test-hydrophoneId")
      );
      propertyMap.put(
          "preampId", Optional.of("test-preampId")
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
    Map<String, Optional<String>> propertyMap = new java.util.HashMap<>(Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name"),
        "description", Optional.of("test-description"),
        "position.x", Optional.of("testX"),
        "position.y", Optional.of("testY"),
        "position.z", Optional.of("testZ"),
        "type", Optional.of(type)
    ));

    if (type.equals("other")) {
      propertyMap.put(
          "properties", Optional.of("test-properties")
      );
      propertyMap.put(
          "sensorType", Optional.of("test-type")
      );
    } else if (type.equals("audio")) {
      propertyMap.put(
          "hydrophoneId", Optional.of("test-hydrophoneId")
      );
      propertyMap.put(
          "preampId", Optional.of("test-preampId")
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
    
    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Collections.emptyMap(), TestSensor.class, 1));
    assertEquals(String.format(
        "Translation not supported for %s", TestSensor.class.getSimpleName()
    ), exception.getMessage());
  }
  
  @Test
  void testValidateInstrumentTranslatorPass() {
    TabularTranslator<?> translator = new TestTranslator(List.of(
        new TestTranslatorField("name", 1),
        new TestTranslatorField("fileTypes", 2),
        new TestTranslatorField("uuid", 3)
    ));
    
    
    assertDoesNotThrow(() -> TranslatorUtils.validateTranslator(translator, Instrument.class));
  }
  
  @Test
  void testValidateInstrumentTranslatorMissingField() {
    TabularTranslator<?> translator = new TestTranslator(List.of(
        new TestTranslatorField("name", 1),
        new TestTranslatorField("fileTypes", 2)
    ));
    
    TranslatorValidationException exception = assertThrows(TranslatorValidationException.class, () -> TranslatorUtils.validateTranslator(translator, Instrument.class));
    assertEquals("Translator does not fully describe Instrument. Missing fields: [uuid]", exception.getMessage());
  }
  
  @Test
  void testTranslateInstrumentMissingRepository() {
    Exception exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Collections.emptyMap(), Instrument.class, 1, mock(PersonRepository.class)));
    assertEquals("Instrument translation missing fileType repository", exception.getMessage());

    exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Collections.emptyMap(), Instrument.class, 1));
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
        "uuid", Optional.empty(),
        "name", Optional.of("test-name"),
        "fileTypes", Optional.of(String.format(
            "%s;%s", fileType1, fileType2
        ))
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
        "uuid", Optional.empty(),
        "name", Optional.of("test-name"),
        "fileType", Optional.empty()
    ), Instrument.class, 1, fileTypeRepository);

    assertNull(instrument.getUuid());
    assertEquals("test-name", instrument.getName());
    assertEquals(0, instrument.getFileTypes().size());
  }
  
  @Test
  void testTranslateInstrumentBadUUID() {
    FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "uuid", Optional.of("TEST-UUID"),
        "name", Optional.of("test-name"),
        "fileType", Optional.empty()
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
        "uuid", Optional.empty(),
        "name", Optional.of("test-name"),
        "fileTypes", Optional.of(String.format(
            "%s;%s", fileType1, fileType2
        ))
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
        "uuid", Optional.empty(),
        "name", Optional.of("test-name"),
        "fileTypes", Optional.of(String.format(
            "%s;%s", fileType1, fileType2
        ))
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
  void testInvalidFileTypeTranslator() {
    TestTranslator translator = new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1)
    ));
    
    Exception exception = assertThrows(TranslatorValidationException.class, () -> TranslatorUtils.validateTranslator(translator, FileType.class));
    assertEquals("Translator does not fully describe FileType. Missing fields: [comment, type]", exception.getMessage());
  }

  @Test
  void testValidFileTypeTranslator() {
    TestTranslator translator = new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("type", 2),
        new TestTranslatorField("comment", 3)
    ));

    assertDoesNotThrow(() -> TranslatorUtils.validateTranslator(translator, FileType.class));
  }
  
  @Test
  void testTranslateAudioDataset() throws RowConversionException, NotFoundException, DatastoreException {
    
    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("temperaturePath", Optional.of("temperaturePath"));
    values.put("documentsPath", Optional.of("documentsPath"));
    values.put("otherPath", Optional.of("otherPath"));
    values.put("navigationPath", Optional.of("navigationPath"));
    values.put("calibrationDocumentsPath", Optional.of("calibrationDocumentsPath"));
    values.put("sourcePath", Optional.of("sourcePath"));
    values.put("biologicalPath", Optional.of("biologicalPath"));
    values.put("datasetType", Optional.of("audio"));
    values.put("siteOrCruiseName", Optional.of("site-or-cruise-name"));
    values.put("deploymentId", Optional.of("deployment-id"));
    values.put("datasetPackager", Optional.of("dataset-packager"));
    values.put("projects", Optional.of("project-1;project-2"));
    values.put("publicReleaseDate", Optional.of(LocalDate.now().toString()));
    values.put("scientists", Optional.of("scientist-1;scientist-2"));
    values.put("sponsors", Optional.of("sponsor-1;sponsor-2"));
    values.put("funders", Optional.of("funder-1;funder-2"));
    values.put("platform", Optional.of("platform"));
    values.put("instrument", Optional.of("instrument"));
    values.put("instrumentId", Optional.of("instrumentId"));
    values.put("startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("preDeploymentCalibrationDate", Optional.of(LocalDate.now().minusDays(14).toString()));
    values.put("postDeploymentCalibrationDate", Optional.of(LocalDate.now().toString()));
    values.put("calibrationDescription", Optional.of("calibration-description"));
    values.put("hydrophoneSensitivity", Optional.of("10.0"));
    values.put("frequencyRange", Optional.of("5.0"));
    values.put("gain", Optional.of("1.0"));
    values.put("deploymentTitle", Optional.of("deployment-title"));
    values.put("deploymentPurpose", Optional.of("deployment-purpose"));
    values.put("deploymentDescription", Optional.of("deployment-description"));
    values.put("alternateSiteName", Optional.of("alternate-site-name"));
    values.put("alternateDeploymentName", Optional.of("alternate-deployment-name"));
    values.put("qualityAnalyst", Optional.of("quality-analyst"));
    values.put("qualityAnalysisObjectives", Optional.of("quality-analysis-objectives"));
    values.put("qualityAnalysisMethod", Optional.of("quality-analysis-method"));
    values.put("qualityAssessmentDescription", Optional.of("quality-assessment-description"));
    values.put("qualityEntries[0].startTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("qualityEntries[0].endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("qualityEntries[0].minFrequency", Optional.of("100.0"));
    values.put("qualityEntries[0].maxFrequency", Optional.of("200.0"));
    values.put("qualityEntries[0].qualityLevel", Optional.of("Good"));
    values.put("qualityEntries[0].comments", Optional.of("quality-comment-1"));
    values.put("qualityEntries[1].startTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("qualityEntries[1].endTime", Optional.of(LocalDateTime.now().plusDays(1).toString()));
    values.put("qualityEntries[1].minFrequency", Optional.of("300.0"));
    values.put("qualityEntries[1].maxFrequency", Optional.of("400.0"));
    values.put("qualityEntries[1].qualityLevel", Optional.of("Unusable"));
    values.put("qualityEntries[1].comments", Optional.of("quality-comment-2"));
    values.put("deploymentTime", Optional.of(LocalDateTime.now().minusDays(10).toString()));
    values.put("recoveryTime", Optional.of(LocalDateTime.now().toString()));
    values.put("comments", Optional.of("deployment-comments"));
    values.put("sensors", Optional.of("sensor-1;sensor-2"));
    values.put("channels[0].sensor", Optional.of("sensor-1"));
    values.put("channels[0].startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("channels[0].endTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[0].sampleRates[0].startTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[0].sampleRates[0].endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("channels[0].sampleRates[0].sampleRate", Optional.of("1.0"));
    values.put("channels[0].sampleRates[0].sampleBits", Optional.of("2"));
    values.put("channels[0].sampleRates[1].startTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("channels[0].sampleRates[1].endTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("channels[0].sampleRates[1].sampleRate", Optional.of("3.0"));
    values.put("channels[0].sampleRates[1].sampleBits", Optional.of("4"));
    values.put("channels[0].dutyCycles[0].startTime", Optional.of(LocalDateTime.now().minusDays(10).toString()));
    values.put("channels[0].dutyCycles[0].endTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("channels[0].dutyCycles[0].duration", Optional.of("10.0"));
    values.put("channels[0].dutyCycles[0].interval", Optional.of("11.0"));
    values.put("channels[0].dutyCycles[1].startTime", Optional.of(LocalDateTime.now().minusDays(8).toString()));
    values.put("channels[0].dutyCycles[1].endTime", Optional.of(LocalDateTime.now().minusDays(6).toString()));
    values.put("channels[0].dutyCycles[1].duration", Optional.of("12.0"));
    values.put("channels[0].dutyCycles[1].interval", Optional.of("13.0"));
    values.put("channels[0].gains[0].startTime", Optional.of(LocalDateTime.now().minusDays(100).toString()));
    values.put("channels[0].gains[0].endTime", Optional.of(LocalDateTime.now().minusDays(50).toString()));
    values.put("channels[0].gains[0].gain", Optional.of("100.0"));
    values.put("channels[0].gains[1].startTime", Optional.of(LocalDateTime.now().minusDays(200).toString()));
    values.put("channels[0].gains[1].endTime", Optional.of(LocalDateTime.now().minusDays(150).toString()));
    values.put("channels[0].gains[1].gain", Optional.of("200.0"));
    values.put("channels[1].sensor", Optional.of("sensor-2"));
    values.put("channels[1].startTime", Optional.of(LocalDateTime.now().minusDays(21).toString()));
    values.put("channels[1].endTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("channels[1].sampleRates[0].startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("channels[1].sampleRates[0].endTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[1].sampleRates[0].sampleRate", Optional.of("5.0"));
    values.put("channels[1].sampleRates[0].sampleBits", Optional.of("6"));
    values.put("channels[1].sampleRates[1].startTime", Optional.of(LocalDateTime.now().minusDays(4).toString()));
    values.put("channels[1].sampleRates[1].endTime", Optional.of(LocalDateTime.now().minusDays(3).toString()));
    values.put("channels[1].sampleRates[1].sampleRate", Optional.of("7.0"));
    values.put("channels[1].sampleRates[1].sampleBits", Optional.of("8"));
    values.put("channels[1].dutyCycles[0].startTime", Optional.of(LocalDateTime.now().minusDays(11).toString()));
    values.put("channels[1].dutyCycles[0].endTime", Optional.of(LocalDateTime.now().minusDays(3).toString()));
    values.put("channels[1].dutyCycles[0].duration", Optional.of("11.0"));
    values.put("channels[1].dutyCycles[0].interval", Optional.of("12.0"));
    values.put("channels[1].dutyCycles[1].startTime", Optional.of(LocalDateTime.now().minusDays(9).toString()));
    values.put("channels[1].dutyCycles[1].endTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[1].dutyCycles[1].duration", Optional.of("13.0"));
    values.put("channels[1].dutyCycles[1].interval", Optional.of("14.0"));
    values.put("channels[1].gains[0].startTime", Optional.of(LocalDateTime.now().minusDays(101).toString()));
    values.put("channels[1].gains[0].endTime", Optional.of(LocalDateTime.now().minusDays(51).toString()));
    values.put("channels[1].gains[0].gain", Optional.of("101.0"));
    values.put("channels[1].gains[1].startTime", Optional.of(LocalDateTime.now().minusDays(201).toString()));
    values.put("channels[1].gains[1].endTime", Optional.of(LocalDateTime.now().minusDays(151).toString()));
    values.put("channels[1].gains[1].gain", Optional.of("201.0"));

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
    assertEquals(values.get("temperaturePath").orElseThrow(), audioPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").orElseThrow(), audioPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").orElseThrow(), audioPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").orElseThrow(), audioPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").orElseThrow(), audioPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").orElseThrow(), audioPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").orElseThrow(), audioPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").orElseThrow(), audioPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").orElseThrow(), audioPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").orElseThrow(), audioPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getProjects().get(0).getName(), audioPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").orElseThrow(), audioPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getScientists().get(0).getName(), audioPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getSponsors().get(0).getName(), audioPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getFunders().get(0).getName(), audioPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").orElseThrow(), audioPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").orElseThrow(), audioPackingJob.getInstrument().getName());
    assertEquals(values.get("instrumentId").orElseThrow(), audioPackingJob.getInstrumentId());
    assertEquals(values.get("startTime").orElseThrow(), audioPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").orElseThrow(), audioPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").orElseThrow(), audioPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").orElseThrow(), audioPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").orElseThrow(), audioPackingJob.getCalibrationDescription());
    assertEquals(values.get("hydrophoneSensitivity").orElseThrow(), audioPackingJob.getHydrophoneSensitivity().toString());
    assertEquals(values.get("frequencyRange").orElseThrow(), audioPackingJob.getFrequencyRange().toString());
    assertEquals(values.get("gain").orElseThrow(), audioPackingJob.getGain().toString());
    assertEquals(values.get("deploymentTitle").orElseThrow(), audioPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").orElseThrow(), audioPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").orElseThrow(), audioPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").orElseThrow(), audioPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").orElseThrow(), audioPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("qualityAnalyst").orElseThrow(), audioPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").orElseThrow(), audioPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").orElseThrow(), audioPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").orElseThrow(), audioPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").orElseThrow(), audioPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").orElseThrow(), audioPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").orElseThrow(), audioPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").orElseThrow(), audioPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").orElseThrow(), audioPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").orElseThrow(), audioPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").orElseThrow(), audioPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").orElseThrow(), audioPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").orElseThrow(), audioPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").orElseThrow(), audioPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").orElseThrow(), audioPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").orElseThrow(), audioPackingJob.getQualityEntries().get(1).getComments());
    assertEquals(values.get("deploymentTime").orElseThrow(), audioPackingJob.getDeploymentTime().toString());
    assertEquals(values.get("recoveryTime").orElseThrow(), audioPackingJob.getRecoveryTime().toString());
    assertEquals(values.get("comments").orElseThrow(), audioPackingJob.getComments());
    assertEquals(values.get("sensors").orElseThrow(), String.format(
        "%s;%s", audioPackingJob.getSensors().get(0).getName(), audioPackingJob.getSensors().get(1).getName()
    ));
    assertEquals(values.get("channels[0].sensor").orElseThrow(), audioPackingJob.getChannels().get(0).getSensor().getName());
    assertEquals(values.get("channels[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleRate").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleBits").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[0].sampleRates[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleRate").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleBits").orElseThrow(), audioPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].duration").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].interval").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].duration").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].interval").orElseThrow(), audioPackingJob.getChannels().get(0).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[0].gains[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[0].gain").orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[0].gains[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[1].gain").orElseThrow(), audioPackingJob.getChannels().get(0).getGains().get(1).getGain().toString());
    assertEquals(values.get("channels[1].sensor").orElseThrow(), audioPackingJob.getChannels().get(1).getSensor().getName());
    assertEquals(values.get("channels[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleRate").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleBits").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[1].sampleRates[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleRate").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleBits").orElseThrow(), audioPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].duration").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].interval").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].duration").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].interval").orElseThrow(), audioPackingJob.getChannels().get(1).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[1].gains[0].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[0].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[0].gain").orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[1].gains[1].startTime").orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[1].endTime").orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[1].gain").orElseThrow(), audioPackingJob.getChannels().get(1).getGains().get(1).getGain().toString());
  }

  @Test
  void testTranslateCPODDataset() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("temperaturePath", Optional.of("temperaturePath"));
    values.put("documentsPath", Optional.of("documentsPath"));
    values.put("otherPath", Optional.of("otherPath"));
    values.put("navigationPath", Optional.of("navigationPath"));
    values.put("calibrationDocumentsPath", Optional.of("calibrationDocumentsPath"));
    values.put("sourcePath", Optional.of("sourcePath"));
    values.put("biologicalPath", Optional.of("biologicalPath"));
    values.put("datasetType", Optional.of("CPOD"));
    values.put("siteOrCruiseName", Optional.of("site-or-cruise-name"));
    values.put("deploymentId", Optional.of("deployment-id"));
    values.put("datasetPackager", Optional.of("dataset-packager"));
    values.put("projects", Optional.of("project-1;project-2"));
    values.put("publicReleaseDate", Optional.of(LocalDate.now().toString()));
    values.put("scientists", Optional.of("scientist-1;scientist-2"));
    values.put("sponsors", Optional.of("sponsor-1;sponsor-2"));
    values.put("funders", Optional.of("funder-1;funder-2"));
    values.put("platform", Optional.of("platform"));
    values.put("instrument", Optional.of("instrument"));
    values.put("instrumentId", Optional.of("instrumentId"));
    values.put("startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("preDeploymentCalibrationDate", Optional.of(LocalDate.now().minusDays(14).toString()));
    values.put("postDeploymentCalibrationDate", Optional.of(LocalDate.now().toString()));
    values.put("calibrationDescription", Optional.of("calibration-description"));
    values.put("hydrophoneSensitivity", Optional.of("10.0"));
    values.put("frequencyRange", Optional.of("5.0"));
    values.put("gain", Optional.of("1.0"));
    values.put("deploymentTitle", Optional.of("deployment-title"));
    values.put("deploymentPurpose", Optional.of("deployment-purpose"));
    values.put("deploymentDescription", Optional.of("deployment-description"));
    values.put("alternateSiteName", Optional.of("alternate-site-name"));
    values.put("alternateDeploymentName", Optional.of("alternate-deployment-name"));
    values.put("qualityAnalyst", Optional.of("quality-analyst"));
    values.put("qualityAnalysisObjectives", Optional.of("quality-analysis-objectives"));
    values.put("qualityAnalysisMethod", Optional.of("quality-analysis-method"));
    values.put("qualityAssessmentDescription", Optional.of("quality-assessment-description"));
    values.put("qualityEntries[0].startTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("qualityEntries[0].endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("qualityEntries[0].minFrequency", Optional.of("100.0"));
    values.put("qualityEntries[0].maxFrequency", Optional.of("200.0"));
    values.put("qualityEntries[0].qualityLevel", Optional.of("Good"));
    values.put("qualityEntries[0].comments", Optional.of("quality-comment-1"));
    values.put("qualityEntries[1].startTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("qualityEntries[1].endTime", Optional.of(LocalDateTime.now().plusDays(1).toString()));
    values.put("qualityEntries[1].minFrequency", Optional.of("300.0"));
    values.put("qualityEntries[1].maxFrequency", Optional.of("400.0"));
    values.put("qualityEntries[1].qualityLevel", Optional.of("Unusable"));
    values.put("qualityEntries[1].comments", Optional.of("quality-comment-2"));
    values.put("deploymentTime", Optional.of(LocalDateTime.now().minusDays(10).toString()));
    values.put("recoveryTime", Optional.of(LocalDateTime.now().toString()));
    values.put("comments", Optional.of("deployment-comments"));
    values.put("sensors", Optional.of("sensor-1;sensor-2"));
    values.put("channels[0].sensor", Optional.of("sensor-1"));
    values.put("channels[0].startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("channels[0].endTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[0].sampleRates[0].startTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[0].sampleRates[0].endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("channels[0].sampleRates[0].sampleRate", Optional.of("1.0"));
    values.put("channels[0].sampleRates[0].sampleBits", Optional.of("2"));
    values.put("channels[0].sampleRates[1].startTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("channels[0].sampleRates[1].endTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("channels[0].sampleRates[1].sampleRate", Optional.of("3.0"));
    values.put("channels[0].sampleRates[1].sampleBits", Optional.of("4"));
    values.put("channels[0].dutyCycles[0].startTime", Optional.of(LocalDateTime.now().minusDays(10).toString()));
    values.put("channels[0].dutyCycles[0].endTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("channels[0].dutyCycles[0].duration", Optional.of("10.0"));
    values.put("channels[0].dutyCycles[0].interval", Optional.of("11.0"));
    values.put("channels[0].dutyCycles[1].startTime", Optional.of(LocalDateTime.now().minusDays(8).toString()));
    values.put("channels[0].dutyCycles[1].endTime", Optional.of(LocalDateTime.now().minusDays(6).toString()));
    values.put("channels[0].dutyCycles[1].duration", Optional.of("12.0"));
    values.put("channels[0].dutyCycles[1].interval", Optional.of("13.0"));
    values.put("channels[0].gains[0].startTime", Optional.of(LocalDateTime.now().minusDays(100).toString()));
    values.put("channels[0].gains[0].endTime", Optional.of(LocalDateTime.now().minusDays(50).toString()));
    values.put("channels[0].gains[0].gain", Optional.of("100.0"));
    values.put("channels[0].gains[1].startTime", Optional.of(LocalDateTime.now().minusDays(200).toString()));
    values.put("channels[0].gains[1].endTime", Optional.of(LocalDateTime.now().minusDays(150).toString()));
    values.put("channels[0].gains[1].gain", Optional.of("200.0"));
    values.put("channels[1].sensor", Optional.of("sensor-2"));
    values.put("channels[1].startTime", Optional.of(LocalDateTime.now().minusDays(21).toString()));
    values.put("channels[1].endTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("channels[1].sampleRates[0].startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("channels[1].sampleRates[0].endTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[1].sampleRates[0].sampleRate", Optional.of("5.0"));
    values.put("channels[1].sampleRates[0].sampleBits", Optional.of("6"));
    values.put("channels[1].sampleRates[1].startTime", Optional.of(LocalDateTime.now().minusDays(4).toString()));
    values.put("channels[1].sampleRates[1].endTime", Optional.of(LocalDateTime.now().minusDays(3).toString()));
    values.put("channels[1].sampleRates[1].sampleRate", Optional.of("7.0"));
    values.put("channels[1].sampleRates[1].sampleBits", Optional.of("8"));
    values.put("channels[1].dutyCycles[0].startTime", Optional.of(LocalDateTime.now().minusDays(11).toString()));
    values.put("channels[1].dutyCycles[0].endTime", Optional.of(LocalDateTime.now().minusDays(3).toString()));
    values.put("channels[1].dutyCycles[0].duration", Optional.of("11.0"));
    values.put("channels[1].dutyCycles[0].interval", Optional.of("12.0"));
    values.put("channels[1].dutyCycles[1].startTime", Optional.of(LocalDateTime.now().minusDays(9).toString()));
    values.put("channels[1].dutyCycles[1].endTime", Optional.of(LocalDateTime.now().minusDays(7).toString()));
    values.put("channels[1].dutyCycles[1].duration", Optional.of("13.0"));
    values.put("channels[1].dutyCycles[1].interval", Optional.of("14.0"));
    values.put("channels[1].gains[0].startTime", Optional.of(LocalDateTime.now().minusDays(101).toString()));
    values.put("channels[1].gains[0].endTime", Optional.of(LocalDateTime.now().minusDays(51).toString()));
    values.put("channels[1].gains[0].gain", Optional.of("101.0"));
    values.put("channels[1].gains[1].startTime", Optional.of(LocalDateTime.now().minusDays(201).toString()));
    values.put("channels[1].gains[1].endTime", Optional.of(LocalDateTime.now().minusDays(151).toString()));
    values.put("channels[1].gains[1].gain", Optional.of("201.0"));

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
    assertEquals(values.get("temperaturePath").orElseThrow(), cpodPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").orElseThrow(), cpodPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").orElseThrow(), cpodPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").orElseThrow(), cpodPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").orElseThrow(), cpodPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").orElseThrow(), cpodPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").orElseThrow(), cpodPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").orElseThrow(), cpodPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").orElseThrow(), cpodPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").orElseThrow(), cpodPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getProjects().get(0).getName(), cpodPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").orElseThrow(), cpodPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getScientists().get(0).getName(), cpodPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getSponsors().get(0).getName(), cpodPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getFunders().get(0).getName(), cpodPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").orElseThrow(), cpodPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").orElseThrow(), cpodPackingJob.getInstrument().getName());
    assertEquals(values.get("instrumentId").orElseThrow(), cpodPackingJob.getInstrumentId());
    assertEquals(values.get("startTime").orElseThrow(), cpodPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").orElseThrow(), cpodPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").orElseThrow(), cpodPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").orElseThrow(), cpodPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").orElseThrow(), cpodPackingJob.getCalibrationDescription());
    assertEquals(values.get("hydrophoneSensitivity").orElseThrow(), cpodPackingJob.getHydrophoneSensitivity().toString());
    assertEquals(values.get("frequencyRange").orElseThrow(), cpodPackingJob.getFrequencyRange().toString());
    assertEquals(values.get("gain").orElseThrow(), cpodPackingJob.getGain().toString());
    assertEquals(values.get("deploymentTitle").orElseThrow(), cpodPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").orElseThrow(), cpodPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").orElseThrow(), cpodPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").orElseThrow(), cpodPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").orElseThrow(), cpodPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("qualityAnalyst").orElseThrow(), cpodPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").orElseThrow(), cpodPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").orElseThrow(), cpodPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").orElseThrow(), cpodPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").orElseThrow(), cpodPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").orElseThrow(), cpodPackingJob.getQualityEntries().get(1).getComments());
    assertEquals(values.get("deploymentTime").orElseThrow(), cpodPackingJob.getDeploymentTime().toString());
    assertEquals(values.get("recoveryTime").orElseThrow(), cpodPackingJob.getRecoveryTime().toString());
    assertEquals(values.get("comments").orElseThrow(), cpodPackingJob.getComments());
    assertEquals(values.get("sensors").orElseThrow(), String.format(
        "%s;%s", cpodPackingJob.getSensors().get(0).getName(), cpodPackingJob.getSensors().get(1).getName()
    ));
    assertEquals(values.get("channels[0].sensor").orElseThrow(), cpodPackingJob.getChannels().get(0).getSensor().getName());
    assertEquals(values.get("channels[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleRate").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[0].sampleBits").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[0].sampleRates[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleRate").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[0].sampleRates[1].sampleBits").orElseThrow(), cpodPackingJob.getChannels().get(0).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].duration").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[0].interval").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].duration").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[0].dutyCycles[1].interval").orElseThrow(), cpodPackingJob.getChannels().get(0).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[0].gains[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[0].gain").orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[0].gains[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[0].gains[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[0].gains[1].gain").orElseThrow(), cpodPackingJob.getChannels().get(0).getGains().get(1).getGain().toString());
    assertEquals(values.get("channels[1].sensor").orElseThrow(), cpodPackingJob.getChannels().get(1).getSensor().getName());
    assertEquals(values.get("channels[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleRate").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[0].sampleBits").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(0).getSampleBits().toString());
    assertEquals(values.get("channels[1].sampleRates[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleRate").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleRate().toString());
    assertEquals(values.get("channels[1].sampleRates[1].sampleBits").orElseThrow(), cpodPackingJob.getChannels().get(1).getSampleRates().get(1).getSampleBits().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].duration").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[0].interval").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(0).getInterval().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].duration").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getDuration().toString());
    assertEquals(values.get("channels[1].dutyCycles[1].interval").orElseThrow(), cpodPackingJob.getChannels().get(1).getDutyCycles().get(1).getInterval().toString());
    assertEquals(values.get("channels[1].gains[0].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(0).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[0].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(0).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[0].gain").orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(0).getGain().toString());
    assertEquals(values.get("channels[1].gains[1].startTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(1).getStartTime().toString());
    assertEquals(values.get("channels[1].gains[1].endTime").orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(1).getEndTime().toString());
    assertEquals(values.get("channels[1].gains[1].gain").orElseThrow(), cpodPackingJob.getChannels().get(1).getGains().get(1).getGain().toString());
  }

  @Test
  void testTranslateSoundClipsDataset() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("temperaturePath", Optional.of("temperaturePath"));
    values.put("documentsPath", Optional.of("documentsPath"));
    values.put("otherPath", Optional.of("otherPath"));
    values.put("navigationPath", Optional.of("navigationPath"));
    values.put("calibrationDocumentsPath", Optional.of("calibrationDocumentsPath"));
    values.put("sourcePath", Optional.of("sourcePath"));
    values.put("biologicalPath", Optional.of("biologicalPath"));
    values.put("datasetType", Optional.of("sound clips"));
    values.put("siteOrCruiseName", Optional.of("site-or-cruise-name"));
    values.put("deploymentId", Optional.of("deployment-id"));
    values.put("datasetPackager", Optional.of("dataset-packager"));
    values.put("projects", Optional.of("project-1;project-2"));
    values.put("publicReleaseDate", Optional.of(LocalDate.now().toString()));
    values.put("scientists", Optional.of("scientist-1;scientist-2"));
    values.put("sponsors", Optional.of("sponsor-1;sponsor-2"));
    values.put("funders", Optional.of("funder-1;funder-2"));
    values.put("platform", Optional.of("platform"));
    values.put("instrument", Optional.of("instrument"));
    values.put("startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("preDeploymentCalibrationDate", Optional.of(LocalDate.now().minusDays(14).toString()));
    values.put("postDeploymentCalibrationDate", Optional.of(LocalDate.now().toString()));
    values.put("calibrationDescription", Optional.of("calibration-description"));
    values.put("deploymentTitle", Optional.of("deployment-title"));
    values.put("deploymentPurpose", Optional.of("deployment-purpose"));
    values.put("deploymentDescription", Optional.of("deployment-description"));
    values.put("alternateSiteName", Optional.of("alternate-site-name"));
    values.put("alternateDeploymentName", Optional.of("alternate-deployment-name"));
    values.put("softwareNames", Optional.of("software-names"));
    values.put("softwareVersions", Optional.of("software-versions"));
    values.put("softwareProtocolCitation", Optional.of("software-protocol-citation"));
    values.put("softwareDescription", Optional.of("software-description"));
    values.put("softwareProcessingDescription", Optional.of("software-processing-description"));

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
    assertEquals(values.get("temperaturePath").orElseThrow(), soundClipsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").orElseThrow(), soundClipsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").orElseThrow(), soundClipsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").orElseThrow(), soundClipsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").orElseThrow(), soundClipsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").orElseThrow(), soundClipsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").orElseThrow(), soundClipsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").orElseThrow(), soundClipsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").orElseThrow(), soundClipsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").orElseThrow(), soundClipsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getProjects().get(0).getName(), soundClipsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").orElseThrow(), soundClipsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getScientists().get(0).getName(), soundClipsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getSponsors().get(0).getName(), soundClipsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").orElseThrow(), String.format(
        "%s;%s", soundClipsPackingJob.getFunders().get(0).getName(), soundClipsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").orElseThrow(), soundClipsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").orElseThrow(), soundClipsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").orElseThrow(), soundClipsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").orElseThrow(), soundClipsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").orElseThrow(), soundClipsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").orElseThrow(), soundClipsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").orElseThrow(), soundClipsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").orElseThrow(), soundClipsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").orElseThrow(), soundClipsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").orElseThrow(), soundClipsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").orElseThrow(), soundClipsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").orElseThrow(), soundClipsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").orElseThrow(), soundClipsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").orElseThrow(), soundClipsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").orElseThrow(), soundClipsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").orElseThrow(), soundClipsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").orElseThrow(), soundClipsPackingJob.getSoftwareProcessingDescription());
  }

  @Test
  void testTranslateDetections() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("temperaturePath", Optional.of("temperaturePath"));
    values.put("documentsPath", Optional.of("documentsPath"));
    values.put("otherPath", Optional.of("otherPath"));
    values.put("navigationPath", Optional.of("navigationPath"));
    values.put("calibrationDocumentsPath", Optional.of("calibrationDocumentsPath"));
    values.put("sourcePath", Optional.of("sourcePath"));
    values.put("biologicalPath", Optional.of("biologicalPath"));
    values.put("datasetType", Optional.of("detections"));
    values.put("siteOrCruiseName", Optional.of("site-or-cruise-name"));
    values.put("deploymentId", Optional.of("deployment-id"));
    values.put("datasetPackager", Optional.of("dataset-packager"));
    values.put("projects", Optional.of("project-1;project-2"));
    values.put("publicReleaseDate", Optional.of(LocalDate.now().toString()));
    values.put("scientists", Optional.of("scientist-1;scientist-2"));
    values.put("sponsors", Optional.of("sponsor-1;sponsor-2"));
    values.put("funders", Optional.of("funder-1;funder-2"));
    values.put("platform", Optional.of("platform"));
    values.put("instrument", Optional.of("instrument"));
    values.put("startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("preDeploymentCalibrationDate", Optional.of(LocalDate.now().minusDays(14).toString()));
    values.put("postDeploymentCalibrationDate", Optional.of(LocalDate.now().toString()));
    values.put("calibrationDescription", Optional.of("calibration-description"));
    values.put("deploymentTitle", Optional.of("deployment-title"));
    values.put("deploymentPurpose", Optional.of("deployment-purpose"));
    values.put("deploymentDescription", Optional.of("deployment-description"));
    values.put("alternateSiteName", Optional.of("alternate-site-name"));
    values.put("alternateDeploymentName", Optional.of("alternate-deployment-name"));
    values.put("softwareNames", Optional.of("software-names"));
    values.put("softwareVersions", Optional.of("software-versions"));
    values.put("softwareProtocolCitation", Optional.of("software-protocol-citation"));
    values.put("softwareDescription", Optional.of("software-description"));
    values.put("softwareProcessingDescription", Optional.of("software-processing-description"));
    values.put("qualityAnalyst", Optional.of("quality-analyst"));
    values.put("qualityAnalysisObjectives", Optional.of("quality-analysis-objectives"));
    values.put("qualityAnalysisMethod", Optional.of("quality-analysis-method"));
    values.put("qualityAssessmentDescription", Optional.of("quality-assessment-description"));
    values.put("qualityEntries[0].startTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("qualityEntries[0].endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("qualityEntries[0].minFrequency", Optional.of("100.0"));
    values.put("qualityEntries[0].maxFrequency", Optional.of("200.0"));
    values.put("qualityEntries[0].qualityLevel", Optional.of("Good"));
    values.put("qualityEntries[0].comments", Optional.of("quality-comment-1"));
    values.put("qualityEntries[1].startTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("qualityEntries[1].endTime", Optional.of(LocalDateTime.now().plusDays(1).toString()));
    values.put("qualityEntries[1].minFrequency", Optional.of("300.0"));
    values.put("qualityEntries[1].maxFrequency", Optional.of("400.0"));
    values.put("qualityEntries[1].qualityLevel", Optional.of("Unusable"));
    values.put("qualityEntries[1].comments", Optional.of("quality-comment-2"));
    values.put("soundSource", Optional.of("sound-source"));
    values.put("analysisTimeZone", Optional.of("1"));
    values.put("analysisEffort", Optional.of("2"));
    values.put("sampleRate", Optional.of("3.0"));
    values.put("minFrequency", Optional.of("4.0"));
    values.put("maxFrequency", Optional.of("5.0"));

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
    assertEquals(values.get("temperaturePath").orElseThrow(), detectionsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").orElseThrow(), detectionsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").orElseThrow(), detectionsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").orElseThrow(), detectionsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").orElseThrow(), detectionsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").orElseThrow(), detectionsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").orElseThrow(), detectionsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").orElseThrow(), detectionsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").orElseThrow(), detectionsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").orElseThrow(), detectionsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getProjects().get(0).getName(), detectionsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").orElseThrow(), detectionsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getScientists().get(0).getName(), detectionsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getSponsors().get(0).getName(), detectionsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").orElseThrow(), String.format(
        "%s;%s", detectionsPackingJob.getFunders().get(0).getName(), detectionsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").orElseThrow(), detectionsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").orElseThrow(), detectionsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").orElseThrow(), detectionsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").orElseThrow(), detectionsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").orElseThrow(), detectionsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").orElseThrow(), detectionsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").orElseThrow(), detectionsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").orElseThrow(), detectionsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").orElseThrow(), detectionsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").orElseThrow(), detectionsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").orElseThrow(), detectionsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").orElseThrow(), detectionsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").orElseThrow(), detectionsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").orElseThrow(), detectionsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").orElseThrow(), detectionsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").orElseThrow(), detectionsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").orElseThrow(), detectionsPackingJob.getSoftwareProcessingDescription());
    assertEquals(values.get("soundSource").orElseThrow(), detectionsPackingJob.getSoundSource().getSource());
    assertEquals(values.get("analysisTimeZone").orElseThrow(), detectionsPackingJob.getAnalysisTimeZone().toString());
    assertEquals(values.get("analysisEffort").orElseThrow(), detectionsPackingJob.getAnalysisEffort().toString());
    assertEquals(values.get("sampleRate").orElseThrow(), detectionsPackingJob.getSampleRate().toString());
    assertEquals(values.get("minFrequency").orElseThrow(), detectionsPackingJob.getMinFrequency().toString());
    assertEquals(values.get("maxFrequency").orElseThrow(), detectionsPackingJob.getMaxFrequency().toString());
    assertEquals(values.get("qualityAnalyst").orElseThrow(), detectionsPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").orElseThrow(), detectionsPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").orElseThrow(), detectionsPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").orElseThrow(), detectionsPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").orElseThrow(), detectionsPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").orElseThrow(), detectionsPackingJob.getQualityEntries().get(1).getComments());
  }

  @Test
  void testTranslateSoundLevelMetrics() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("temperaturePath", Optional.of("temperaturePath"));
    values.put("documentsPath", Optional.of("documentsPath"));
    values.put("otherPath", Optional.of("otherPath"));
    values.put("navigationPath", Optional.of("navigationPath"));
    values.put("calibrationDocumentsPath", Optional.of("calibrationDocumentsPath"));
    values.put("sourcePath", Optional.of("sourcePath"));
    values.put("biologicalPath", Optional.of("biologicalPath"));
    values.put("datasetType", Optional.of("sound level metrics"));
    values.put("siteOrCruiseName", Optional.of("site-or-cruise-name"));
    values.put("deploymentId", Optional.of("deployment-id"));
    values.put("datasetPackager", Optional.of("dataset-packager"));
    values.put("projects", Optional.of("project-1;project-2"));
    values.put("publicReleaseDate", Optional.of(LocalDate.now().toString()));
    values.put("scientists", Optional.of("scientist-1;scientist-2"));
    values.put("sponsors", Optional.of("sponsor-1;sponsor-2"));
    values.put("funders", Optional.of("funder-1;funder-2"));
    values.put("platform", Optional.of("platform"));
    values.put("instrument", Optional.of("instrument"));
    values.put("startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("preDeploymentCalibrationDate", Optional.of(LocalDate.now().minusDays(14).toString()));
    values.put("postDeploymentCalibrationDate", Optional.of(LocalDate.now().toString()));
    values.put("calibrationDescription", Optional.of("calibration-description"));
    values.put("deploymentTitle", Optional.of("deployment-title"));
    values.put("deploymentPurpose", Optional.of("deployment-purpose"));
    values.put("deploymentDescription", Optional.of("deployment-description"));
    values.put("alternateSiteName", Optional.of("alternate-site-name"));
    values.put("alternateDeploymentName", Optional.of("alternate-deployment-name"));
    values.put("softwareNames", Optional.of("software-names"));
    values.put("softwareVersions", Optional.of("software-versions"));
    values.put("softwareProtocolCitation", Optional.of("software-protocol-citation"));
    values.put("softwareDescription", Optional.of("software-description"));
    values.put("softwareProcessingDescription", Optional.of("software-processing-description"));
    values.put("qualityAnalyst", Optional.of("quality-analyst"));
    values.put("qualityAnalysisObjectives", Optional.of("quality-analysis-objectives"));
    values.put("qualityAnalysisMethod", Optional.of("quality-analysis-method"));
    values.put("qualityAssessmentDescription", Optional.of("quality-assessment-description"));
    values.put("qualityEntries[0].startTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("qualityEntries[0].endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("qualityEntries[0].minFrequency", Optional.of("100.0"));
    values.put("qualityEntries[0].maxFrequency", Optional.of("200.0"));
    values.put("qualityEntries[0].qualityLevel", Optional.of("Good"));
    values.put("qualityEntries[0].comments", Optional.of("quality-comment-1"));
    values.put("qualityEntries[1].startTime", Optional.of(LocalDateTime.now().minusDays(2).toString()));
    values.put("qualityEntries[1].endTime", Optional.of(LocalDateTime.now().plusDays(1).toString()));
    values.put("qualityEntries[1].minFrequency", Optional.of("300.0"));
    values.put("qualityEntries[1].maxFrequency", Optional.of("400.0"));
    values.put("qualityEntries[1].qualityLevel", Optional.of("Unusable"));
    values.put("qualityEntries[1].comments", Optional.of("quality-comment-2"));
    values.put("analysisTimeZone", Optional.of("1"));
    values.put("analysisEffort", Optional.of("2"));
    values.put("sampleRate", Optional.of("3.0"));
    values.put("minFrequency", Optional.of("4.0"));
    values.put("maxFrequency", Optional.of("5.0"));
    values.put("audioStartTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("audioEndTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));

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
    assertEquals(values.get("temperaturePath").orElseThrow(), soundLevelMetricsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").orElseThrow(), soundLevelMetricsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").orElseThrow(), soundLevelMetricsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").orElseThrow(), soundLevelMetricsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").orElseThrow(), soundLevelMetricsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").orElseThrow(), soundLevelMetricsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").orElseThrow(), soundLevelMetricsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").orElseThrow(), soundLevelMetricsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").orElseThrow(), soundLevelMetricsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").orElseThrow(), soundLevelMetricsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getProjects().get(0).getName(), soundLevelMetricsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").orElseThrow(), soundLevelMetricsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getScientists().get(0).getName(), soundLevelMetricsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getSponsors().get(0).getName(), soundLevelMetricsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").orElseThrow(), String.format(
        "%s;%s", soundLevelMetricsPackingJob.getFunders().get(0).getName(), soundLevelMetricsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").orElseThrow(), soundLevelMetricsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").orElseThrow(), soundLevelMetricsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").orElseThrow(), soundLevelMetricsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").orElseThrow(), soundLevelMetricsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").orElseThrow(), soundLevelMetricsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").orElseThrow(), soundLevelMetricsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").orElseThrow(), soundLevelMetricsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").orElseThrow(), soundLevelMetricsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").orElseThrow(), soundLevelMetricsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").orElseThrow(), soundLevelMetricsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").orElseThrow(), soundLevelMetricsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").orElseThrow(), soundLevelMetricsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").orElseThrow(), soundLevelMetricsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").orElseThrow(), soundLevelMetricsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").orElseThrow(), soundLevelMetricsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").orElseThrow(), soundLevelMetricsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").orElseThrow(), soundLevelMetricsPackingJob.getSoftwareProcessingDescription());
    assertEquals(values.get("analysisTimeZone").orElseThrow(), soundLevelMetricsPackingJob.getAnalysisTimeZone().toString());
    assertEquals(values.get("analysisEffort").orElseThrow(), soundLevelMetricsPackingJob.getAnalysisEffort().toString());
    assertEquals(values.get("sampleRate").orElseThrow(), soundLevelMetricsPackingJob.getSampleRate().toString());
    assertEquals(values.get("minFrequency").orElseThrow(), soundLevelMetricsPackingJob.getMinFrequency().toString());
    assertEquals(values.get("maxFrequency").orElseThrow(), soundLevelMetricsPackingJob.getMaxFrequency().toString());
    assertEquals(values.get("audioStartTime").orElseThrow(), soundLevelMetricsPackingJob.getAudioStartTime().toString());
    assertEquals(values.get("audioEndTime").orElseThrow(), soundLevelMetricsPackingJob.getAudioEndTime().toString());
    assertEquals(values.get("qualityAnalyst").orElseThrow(), soundLevelMetricsPackingJob.getQualityAnalyst().getName());
    assertEquals(values.get("qualityAnalysisObjectives").orElseThrow(), soundLevelMetricsPackingJob.getQualityAnalysisObjectives());
    assertEquals(values.get("qualityAnalysisMethod").orElseThrow(), soundLevelMetricsPackingJob.getQualityAnalysisMethod());
    assertEquals(values.get("qualityAssessmentDescription").orElseThrow(), soundLevelMetricsPackingJob.getQualityAssessmentDescription());
    assertEquals(values.get("qualityEntries[0].startTime").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getStartTime().toString());
    assertEquals(values.get("qualityEntries[0].endTime").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getEndTime().toString());
    assertEquals(values.get("qualityEntries[0].minFrequency").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[0].maxFrequency").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[0].qualityLevel").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[0].comments").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(0).getComments());
    assertEquals(values.get("qualityEntries[1].startTime").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getStartTime().toString());
    assertEquals(values.get("qualityEntries[1].endTime").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getEndTime().toString());
    assertEquals(values.get("qualityEntries[1].minFrequency").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getMinFrequency().toString());
    assertEquals(values.get("qualityEntries[1].maxFrequency").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getMaxFrequency().toString());
    assertEquals(values.get("qualityEntries[1].qualityLevel").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getQualityLevel().getName());
    assertEquals(values.get("qualityEntries[1].comments").orElseThrow(), soundLevelMetricsPackingJob.getQualityEntries().get(1).getComments());
  }

  @Test
  void testTranslateSoundPropagationModels() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("temperaturePath", Optional.of("temperaturePath"));
    values.put("documentsPath", Optional.of("documentsPath"));
    values.put("otherPath", Optional.of("otherPath"));
    values.put("navigationPath", Optional.of("navigationPath"));
    values.put("calibrationDocumentsPath", Optional.of("calibrationDocumentsPath"));
    values.put("sourcePath", Optional.of("sourcePath"));
    values.put("biologicalPath", Optional.of("biologicalPath"));
    values.put("datasetType", Optional.of("sound propagation models"));
    values.put("siteOrCruiseName", Optional.of("site-or-cruise-name"));
    values.put("deploymentId", Optional.of("deployment-id"));
    values.put("datasetPackager", Optional.of("dataset-packager"));
    values.put("projects", Optional.of("project-1;project-2"));
    values.put("publicReleaseDate", Optional.of(LocalDate.now().toString()));
    values.put("scientists", Optional.of("scientist-1;scientist-2"));
    values.put("sponsors", Optional.of("sponsor-1;sponsor-2"));
    values.put("funders", Optional.of("funder-1;funder-2"));
    values.put("platform", Optional.of("platform"));
    values.put("instrument", Optional.of("instrument"));
    values.put("startTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("endTime", Optional.of(LocalDateTime.now().toString()));
    values.put("preDeploymentCalibrationDate", Optional.of(LocalDate.now().minusDays(14).toString()));
    values.put("postDeploymentCalibrationDate", Optional.of(LocalDate.now().toString()));
    values.put("calibrationDescription", Optional.of("calibration-description"));
    values.put("deploymentTitle", Optional.of("deployment-title"));
    values.put("deploymentPurpose", Optional.of("deployment-purpose"));
    values.put("deploymentDescription", Optional.of("deployment-description"));
    values.put("alternateSiteName", Optional.of("alternate-site-name"));
    values.put("alternateDeploymentName", Optional.of("alternate-deployment-name"));
    values.put("softwareNames", Optional.of("software-names"));
    values.put("softwareVersions", Optional.of("software-versions"));
    values.put("softwareProtocolCitation", Optional.of("software-protocol-citation"));
    values.put("softwareDescription", Optional.of("software-description"));
    values.put("softwareProcessingDescription", Optional.of("software-processing-description"));
    values.put("audioStartTime", Optional.of(LocalDateTime.now().minusDays(14).toString()));
    values.put("audioEndTime", Optional.of(LocalDateTime.now().minusDays(1).toString()));
    values.put("modeledFrequency", Optional.of("1.0"));

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
    assertEquals(values.get("temperaturePath").orElseThrow(), soundPropagationModelsPackingJob.getTemperaturePath().toString());
    assertEquals(values.get("documentsPath").orElseThrow(), soundPropagationModelsPackingJob.getDocumentsPath().toString());
    assertEquals(values.get("otherPath").orElseThrow(), soundPropagationModelsPackingJob.getOtherPath().toString());
    assertEquals(values.get("navigationPath").orElseThrow(), soundPropagationModelsPackingJob.getNavigationPath().toString());
    assertEquals(values.get("calibrationDocumentsPath").orElseThrow(), soundPropagationModelsPackingJob.getCalibrationDocumentsPath().toString());
    assertEquals(values.get("sourcePath").orElseThrow(), soundPropagationModelsPackingJob.getSourcePath().toString());
    assertEquals(values.get("biologicalPath").orElseThrow(), soundPropagationModelsPackingJob.getBiologicalPath().toString());
    assertEquals(values.get("siteOrCruiseName").orElseThrow(), soundPropagationModelsPackingJob.getSiteOrCruiseName());
    assertEquals(values.get("deploymentId").orElseThrow(), soundPropagationModelsPackingJob.getDeploymentId());
    assertEquals(values.get("datasetPackager").orElseThrow(), soundPropagationModelsPackingJob.getDatasetPackager().getName());
    assertEquals(values.get("projects").orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getProjects().get(0).getName(), soundPropagationModelsPackingJob.getProjects().get(1).getName()
    ));
    assertEquals(values.get("publicReleaseDate").orElseThrow(), soundPropagationModelsPackingJob.getPublicReleaseDate().toString());
    assertEquals(values.get("scientists").orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getScientists().get(0).getName(), soundPropagationModelsPackingJob.getScientists().get(1).getName()
    ));
    assertEquals(values.get("sponsors").orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getSponsors().get(0).getName(), soundPropagationModelsPackingJob.getSponsors().get(1).getName()
    ));
    assertEquals(values.get("funders").orElseThrow(), String.format(
        "%s;%s", soundPropagationModelsPackingJob.getFunders().get(0).getName(), soundPropagationModelsPackingJob.getFunders().get(1).getName()
    ));
    assertEquals(values.get("platform").orElseThrow(), soundPropagationModelsPackingJob.getPlatform().getName());
    assertEquals(values.get("instrument").orElseThrow(), soundPropagationModelsPackingJob.getInstrument().getName());
    assertEquals(values.get("startTime").orElseThrow(), soundPropagationModelsPackingJob.getStartTime().toString());
    assertEquals(values.get("endTime").orElseThrow(), soundPropagationModelsPackingJob.getEndTime().toString());
    assertEquals(values.get("preDeploymentCalibrationDate").orElseThrow(), soundPropagationModelsPackingJob.getPreDeploymentCalibrationDate().toString());
    assertEquals(values.get("postDeploymentCalibrationDate").orElseThrow(), soundPropagationModelsPackingJob.getPostDeploymentCalibrationDate().toString());
    assertEquals(values.get("calibrationDescription").orElseThrow(), soundPropagationModelsPackingJob.getCalibrationDescription());
    assertEquals(values.get("deploymentTitle").orElseThrow(), soundPropagationModelsPackingJob.getDeploymentTitle());
    assertEquals(values.get("deploymentPurpose").orElseThrow(), soundPropagationModelsPackingJob.getDeploymentPurpose());
    assertEquals(values.get("deploymentDescription").orElseThrow(), soundPropagationModelsPackingJob.getDeploymentDescription());
    assertEquals(values.get("alternateSiteName").orElseThrow(), soundPropagationModelsPackingJob.getAlternateSiteName());
    assertEquals(values.get("alternateDeploymentName").orElseThrow(), soundPropagationModelsPackingJob.getAlternateDeploymentName());
    assertEquals(values.get("softwareNames").orElseThrow(), soundPropagationModelsPackingJob.getSoftwareNames());
    assertEquals(values.get("softwareVersions").orElseThrow(), soundPropagationModelsPackingJob.getSoftwareVersions());
    assertEquals(values.get("softwareProtocolCitation").orElseThrow(), soundPropagationModelsPackingJob.getSoftwareProtocolCitation());
    assertEquals(values.get("softwareDescription").orElseThrow(), soundPropagationModelsPackingJob.getSoftwareDescription());
    assertEquals(values.get("softwareProcessingDescription").orElseThrow(), soundPropagationModelsPackingJob.getSoftwareProcessingDescription());
    assertEquals(values.get("audioStartTime").orElseThrow(), soundPropagationModelsPackingJob.getAudioStartTime().toString());
    assertEquals(values.get("audioEndTime").orElseThrow(), soundPropagationModelsPackingJob.getAudioEndTime().toString());
  }
  
  @Test
  void testInvalidDatasetDateTime() {
    Map<String, Optional<String>> values = Map.of(
        "datasetType", Optional.of("audio"),
        "startTime", Optional.of("TEST")
    );

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
    Map<String, Optional<String>> values = Map.of(
        "datasetType", Optional.of("audio"),
        "publicReleaseDate", Optional.of("TEST")
    );

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
    Map<String, Optional<String>> values = Map.of(
        "datasetType", Optional.of("detections"),
        "analysisEffort", Optional.of("TEST")
    );

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
    Map<String, Optional<String>> values = Map.of(
        "datasetType", Optional.of("audio"),
        "qualityEntries[0].qualityLevel", Optional.of("TEST")
    );

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

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("datasetType", Optional.of("audio"));
    values.put("locationType", Optional.of("stationary marine"));
    values.put("seaArea", Optional.of("sea-area"));
    values.put("deploymentLocation.latitude", Optional.of("1.0"));
    values.put("recoveryLocation.latitude", Optional.of("5.0"));
    values.put("deploymentLocation.longitude", Optional.of("2.0"));
    values.put("recoveryLocation.longitude", Optional.of("6.0"));
    values.put("deploymentLocation.seaFloorDepth", Optional.of("3.0"));
    values.put("recoveryLocation.seaFloorDepth", Optional.of("7.0"));
    values.put("deploymentLocation.instrumentDepth", Optional.of("4.0"));
    values.put("recoveryLocation.instrumentDepth", Optional.of("8.0"));

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
    assertEquals(values.get("seaArea").orElseThrow(), stationaryMarineLocation.getSeaArea().getName());
    assertEquals(values.get("deploymentLocation.latitude").orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getLatitude().toString());
    assertEquals(values.get("recoveryLocation.latitude").orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getLatitude().toString());
    assertEquals(values.get("deploymentLocation.longitude").orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getLongitude().toString());
    assertEquals(values.get("recoveryLocation.longitude").orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getLongitude().toString());
    assertEquals(values.get("deploymentLocation.seaFloorDepth").orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getSeaFloorDepth().toString());
    assertEquals(values.get("recoveryLocation.seaFloorDepth").orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getSeaFloorDepth().toString());
    assertEquals(values.get("deploymentLocation.instrumentDepth").orElseThrow(), stationaryMarineLocation.getDeploymentLocation().getInstrumentDepth().toString());
    assertEquals(values.get("recoveryLocation.instrumentDepth").orElseThrow(), stationaryMarineLocation.getRecoveryLocation().getInstrumentDepth().toString());
  }

  @Test
  void testTranslateMultiPointStationaryMarineLocation() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("datasetType", Optional.of("audio"));
    values.put("locationType", Optional.of("multipoint stationary marine"));
    values.put("seaArea", Optional.of("sea-area"));
    values.put("locations[0].latitude", Optional.of("1.0"));
    values.put("locations[0].longitude", Optional.of("2.0"));
    values.put("locations[0].seaFloorDepth", Optional.of("3.0"));
    values.put("locations[0].instrumentDepth", Optional.of("4.0"));
    values.put("locations[1].latitude", Optional.of("5.0"));
    values.put("locations[1].longitude", Optional.of("6.0"));
    values.put("locations[1].seaFloorDepth", Optional.of("7.0"));
    values.put("locations[1].instrumentDepth", Optional.of("8.0"));

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
    assertEquals(values.get("seaArea").orElseThrow(), multiPointStationaryMarineLocation.getSeaArea().getName());
    assertEquals(values.get("locations[0].latitude").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getLatitude().toString());
    assertEquals(values.get("locations[1].latitude").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getLatitude().toString());
    assertEquals(values.get("locations[0].longitude").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getLongitude().toString());
    assertEquals(values.get("locations[1].longitude").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getLongitude().toString());
    assertEquals(values.get("locations[0].seaFloorDepth").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getSeaFloorDepth().toString());
    assertEquals(values.get("locations[1].seaFloorDepth").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getSeaFloorDepth().toString());
    assertEquals(values.get("locations[0].instrumentDepth").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(0).getInstrumentDepth().toString());
    assertEquals(values.get("locations[1].instrumentDepth").orElseThrow(), multiPointStationaryMarineLocation.getLocations().get(1).getInstrumentDepth().toString());
  }

  @Test
  void testTranslateMobileMarineLocation() throws RowConversionException, NotFoundException, DatastoreException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("datasetType", Optional.of("audio"));
    values.put("locationType", Optional.of("mobile marine"));
    values.put("type", Optional.of("mobile marine"));
    values.put("seaArea", Optional.of("sea-area"));
    values.put("vessel", Optional.of("vessel"));
    values.put("locationDerivationDescription", Optional.of("location-derivation-description"));

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
    assertEquals(values.get("seaArea").orElseThrow(), mobileMarineLocation.getSeaArea().getName());
    assertEquals(values.get("vessel").orElseThrow(), mobileMarineLocation.getVessel().getName());
    assertEquals(values.get("locationDerivationDescription").orElseThrow(), mobileMarineLocation.getLocationDerivationDescription());
  }

  @Test
  void testTranslateStationaryTerrestrialLocation() throws RowConversionException {

    Map<String, Optional<String>> values = new HashMap<>(0);
    values.put("datasetType", Optional.of("audio"));
    values.put("locationType", Optional.of("stationary terrestrial"));
    values.put("latitude", Optional.of("1.0"));
    values.put("longitude", Optional.of("2.0"));
    values.put("surfaceElevation", Optional.of("3.0"));
    values.put("instrumentElevation", Optional.of("4.0"));

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
    assertEquals(values.get("latitude").orElseThrow(), stationaryTerrestrialLocation.getLatitude().toString());
    assertEquals(values.get("longitude").orElseThrow(), stationaryTerrestrialLocation.getLongitude().toString());
    assertEquals(values.get("surfaceElevation").orElseThrow(), stationaryTerrestrialLocation.getSurfaceElevation().toString());
    assertEquals(values.get("instrumentElevation").orElseThrow(), stationaryTerrestrialLocation.getInstrumentElevation().toString());
  }

}
