package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.SoundSource;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.repository.NotFoundException;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.translator.TranslatorExecutorTest.TestTranslator;
import edu.colorado.cires.pace.translator.TranslatorExecutorTest.TestTranslatorField;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TranslatorUtilsTest {
  
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
        "name", Optional.of("test-name"),
        "scientificName", Optional.of("test-scientific-name")
    );

    SoundSource object = TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class, 1);
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
        "name", Optional.of("test-name")
    );

    SoundSource object = TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class, 1);
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
        "name", Optional.of("test-name")
    );

    SoundSource object = TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class, 1);
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

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class, 1));
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

    RowConversionException exception = assertThrows(RowConversionException.class, () -> TranslatorUtils.convertMapToObject(propertyMap, SoundSource.class, 1));
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

}
