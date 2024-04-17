package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.SoundSource;
import edu.colorado.cires.pace.data.validation.ConstraintViolation;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TranslatorUtilsTest {
  
  @Test
  void testUnsupportedType() {
    record TestObject(String name) {}
    
    TranslationException exception = assertThrows(TranslationException.class, () -> TranslatorUtils.convertMapToObject(Map.of(
        "p1", Optional.of("2")
    ), TestObject.class));
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
  void testConvert(Class<? extends ObjectWithName> clazz) throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name")
    );
    
    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSource() throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name"),
        "scientificName", Optional.of("test-scientific-name")
    );

    SoundSource object = TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("uuid not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
    assertEquals(propertyMap.get("scientificName").orElseThrow(
        () -> new IllegalStateException("scientificName not found in propertyMap")
    ), object.getScientificName());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "audio",
      "depth",
      "other"
  })
  void testConvertSensor(String type) throws ValidationException, TranslationException {
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
    
    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class);
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
  void testConvertMissingProperty(Class<? extends ObjectWithName> clazz) throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "name", Optional.of("test-name")
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSourceMissingProperty() throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of(UUID.randomUUID().toString()),
        "name", Optional.of("test-name")
    );

    SoundSource object = TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class);
    assertEquals(propertyMap.get("uuid").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getUuid().toString());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
    assertNull(object.getScientificName());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "audio",
      "depth",
      "other"
  })
  void testConvertMissingSensorProperty(String type) throws ValidationException, TranslationException {
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

    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class);
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
  void testConvertNullUUID(Class<? extends ObjectWithName> clazz) throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.empty(),
        "name", Optional.of("test-name")
    );

    ObjectWithName object = TranslatorUtils.convertMapToObject(propertyMap, clazz);
    assertNull(object.getUuid());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }

  @Test
  void testConvertSoundSourceNullUUID() throws ValidationException, TranslationException {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.empty(),
        "name", Optional.of("test-name")
    );

    SoundSource object = TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class);
    assertNull(object.getUuid());
    assertNull(object.getScientificName());
    assertEquals(propertyMap.get("name").orElseThrow(
        () -> new IllegalStateException("name not found in propertyMap")
    ), object.getName());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "other",
      "audio",
      "depth"
  })
  void testConvertNullSensorUUID(String type) throws ValidationException, TranslationException {
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

    Sensor sensor = TranslatorUtils.convertMapToObject(propertyMap, Sensor.class);
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

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, clazz));
    assertEquals(String.format(
        "%s validation failed", clazz.getSimpleName()
    ), exception.getMessage());
    
    assertEquals(2, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("name"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("name violation not found")
        );
    assertEquals("name", violation.getProperty());
    assertEquals("name must not be blank", violation.getMessage());
  }

  @Test
  void testConvertSoundSourceBadUUIDAndName() {
    Map<String, Optional<String>> propertyMap = Map.of(
        "uuid", Optional.of("test-uuid"),
        "name", Optional.empty()
    );

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class));
    assertEquals(String.format(
        "%s validation failed", SoundSource.class.getSimpleName()
    ), exception.getMessage());

    assertEquals(2, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("name"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("name violation not found")
        );
    assertEquals("name", violation.getProperty());
    assertEquals("name must not be blank", violation.getMessage());
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

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Sensor.class));
    assertEquals(String.format(
        "%s validation failed", Sensor.class.getSimpleName()
    ), exception.getMessage());

    assertEquals(2, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("uuid"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("uuid violation not found")
        );
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("name"))
        .findFirst().orElseThrow(
            () -> new IllegalStateException("name violation not found")
        );
    assertEquals("name", violation.getProperty());
    assertEquals("name must not be blank", violation.getMessage());
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

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, clazz));
    assertEquals(String.format(
        "%s validation failed", clazz.getSimpleName()
    ), exception.getMessage());

    assertEquals(1, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
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

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class));
    assertEquals(String.format(
        "%s validation failed", SoundSource.class.getSimpleName()
    ), exception.getMessage());

    assertEquals(1, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
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

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Sensor.class));
    assertEquals(String.format(
        "%s validation failed", Sensor.class.getSimpleName()
    ), exception.getMessage());

    assertEquals(1, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
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

    ValidationException exception = assertThrows(ValidationException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, Sensor.class));
    assertEquals(String.format(
        "%s validation failed", Sensor.class.getSimpleName()
    ), exception.getMessage());

    assertEquals(3, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("position.x"))
        .findFirst().orElseThrow();
    assertEquals("position.x", violation.getProperty());
    assertEquals("invalid number format", violation.getMessage());

    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("position.y"))
        .findFirst().orElseThrow();
    assertEquals("position.y", violation.getProperty());
    assertEquals("invalid number format", violation.getMessage());

    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("position.z"))
        .findFirst().orElseThrow();
    assertEquals("position.z", violation.getProperty());
    assertEquals("invalid number format", violation.getMessage());
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
    
    TranslationException exception = assertThrows(TranslationException.class, () -> TranslatorUtils.convertMapToObject(Collections.emptyMap(), TestSensor.class));
    assertEquals(String.format(
        "Translation not supported for %s", TestSensor.class.getSimpleName()
    ), exception.getMessage());
  }

}
