package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.ObjectWithName;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TranslatorExecutorTest {
  
  static class TestTranslatorField implements TabularTranslationField {
    private final String propertyName;
    private final int columnNumber;

    TestTranslatorField(String propertyName, int columnNumber) {
      this.propertyName = propertyName;
      this.columnNumber = columnNumber;
    }

    @Override
    public String getPropertyName() {
      return propertyName;
    }

    @Override
    public int getColumnNumber() {
      return columnNumber;
    }

  }
  
  static class TestTranslator implements TabularTranslator<TabularTranslationField> {
    
    private final List<TabularTranslationField> fields;

    TestTranslator(List<TabularTranslationField> fields) {
      this.fields = fields;
    }

    @Override
    public List<TabularTranslationField> getFields() {
      return fields;
    }

    @Override
    public String getName() {
      return "";
    }

    @Override
    public UUID getUuid() {
      return null;
    }
  }
  
  private <O> TranslatorExecutor<O, TabularTranslator<TabularTranslationField>> createExecutor(TestTranslator translator, List<Map<String, Optional<String>>> maps, Class<O> clazz)
      throws TranslatorValidationException {
    return new TranslatorExecutor<>(translator, clazz) {

      @Override
      protected Stream<MapWithRowNumber> getPropertyStream(InputStream inputStream,
          TabularTranslator<TabularTranslationField> translatorDefinition) {
        return maps.stream()
            .map(m -> new MapWithRowNumber(m, 1));
      }

      @Override
      protected Stream<MapWithRowNumber> getPropertyStream(Reader reader,
          TabularTranslator<TabularTranslationField> translatorDefinition) {
        return maps.stream()
            .map(m -> new MapWithRowNumber(m, 1));
      }
    };
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void translate(Class<ObjectWithName> clazz) throws IOException, TranslatorValidationException {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-1")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2")
        )
    );
    
    List<ObjectWithRowConversionException<ObjectWithName>> results = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null).toList();
    assertEquals(2, results.size());
    assertEquals(maps.get(0).get("uuid").orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).rowConversionException());
    assertEquals(maps.get(0).get("name").orElseThrow(), results.get(0).object().getName());
    assertEquals(maps.get(1).get("uuid").orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).rowConversionException());
    assertEquals(maps.get(1).get("name").orElseThrow(), results.get(1).object().getName());
  }

  @Test
  void translateSoundSource() throws IOException, TranslatorValidationException {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-1"),
            "scientificName", Optional.of("test-scientific-name-1")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2"),
            "scientificName", Optional.of("test-scientific-name-2")
        )
    );

    List<ObjectWithRowConversionException<SoundSource>> results = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2),
        new TestTranslatorField("scientificName", 3)
    )), maps, SoundSource.class).translate((InputStream) null).toList();
    assertEquals(2, results.size());
    assertEquals(maps.get(0).get("uuid").orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).rowConversionException());
    assertEquals(maps.get(0).get("name").orElseThrow(), results.get(0).object().getName());
    assertEquals(maps.get(0).get("scientificName").orElseThrow(), results.get(0).object().getScientificName());
    assertEquals(maps.get(1).get("uuid").orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).rowConversionException());
    assertEquals(maps.get(1).get("name").orElseThrow(), results.get(1).object().getName());
    assertEquals(maps.get(1).get("scientificName").orElseThrow(), results.get(1).object().getScientificName());
  }

  @Test
  void translateSensor() throws IOException, TranslatorValidationException {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-1"),
            "description", Optional.of("test-description-1"),
            "position.x", Optional.of("1.0"),
            "position.y", Optional.of("2.0"),
            "position.z", Optional.of("3.0"),
            "properties", Optional.of("test-properties-1"),
            "sensorType", Optional.of("test-sensor-type-1"),
            "type", Optional.of("other")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2"),
            "description", Optional.of("test-description-2"),
            "position.x", Optional.of("1.0"),
            "position.y", Optional.of("2.0"),
            "position.z", Optional.of("3.0"),
            "hydrophoneId", Optional.of("test-hydrophoneId-2"),
            "preampId", Optional.of("test-preampId-2"),
            "type", Optional.of("audio")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-3"),
            "description", Optional.of("test-description-3"),
            "position.x", Optional.of("1.0"),
            "position.y", Optional.of("2.0"),
            "position.z", Optional.of("3.0"),
            "type", Optional.of("depth")
        )
    );
    
    List<TabularTranslationField> fields = new ArrayList<>(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2),
        new TestTranslatorField("description", 3),
        new TestTranslatorField("position.x", 4),
        new TestTranslatorField("position.y", 5),
        new TestTranslatorField("position.z", 6),
        new TestTranslatorField("type", 7),
        new TestTranslatorField("hydrophoneId", 8),
        new TestTranslatorField("preampId", 9),
        new TestTranslatorField("properties", 10),
        new TestTranslatorField("sensorType", 11)
    ));

    List<ObjectWithRowConversionException<Sensor>> results = createExecutor(new TestTranslator(fields), maps, Sensor.class).translate((InputStream) null).toList();
    assertEquals(3, results.size());
    assertEquals(maps.get(0).get("uuid").orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).rowConversionException());
    assertEquals(maps.get(0).get("name").orElseThrow(), results.get(0).object().getName());
    assertEquals(maps.get(0).get("description").orElseThrow(), results.get(0).object().getDescription());
    assertEquals(maps.get(0).get("position.x").orElseThrow(), results.get(0).object().getPosition().getX().toString());
    assertEquals(maps.get(0).get("position.y").orElseThrow(), results.get(0).object().getPosition().getY().toString());
    assertEquals(maps.get(0).get("position.z").orElseThrow(), results.get(0).object().getPosition().getZ().toString());
    assertEquals(maps.get(1).get("uuid").orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).rowConversionException());
    assertEquals(maps.get(1).get("name").orElseThrow(), results.get(1).object().getName());
    assertEquals(maps.get(1).get("description").orElseThrow(), results.get(1).object().getDescription());
    assertEquals(maps.get(1).get("position.x").orElseThrow(), results.get(1).object().getPosition().getX().toString());
    assertEquals(maps.get(1).get("position.y").orElseThrow(), results.get(1).object().getPosition().getY().toString());
    assertEquals(maps.get(1).get("position.z").orElseThrow(), results.get(1).object().getPosition().getZ().toString());
    assertEquals(maps.get(2).get("uuid").orElseThrow(), results.get(2).object().getUuid().toString());
    assertNull(results.get(2).rowConversionException());
    assertEquals(maps.get(2).get("name").orElseThrow(), results.get(2).object().getName());
    assertEquals(maps.get(2).get("description").orElseThrow(), results.get(2).object().getDescription());
    assertEquals(maps.get(2).get("position.x").orElseThrow(), results.get(2).object().getPosition().getX().toString());
    assertEquals(maps.get(2).get("position.y").orElseThrow(), results.get(2).object().getPosition().getY().toString());
    assertEquals(maps.get(2).get("position.z").orElseThrow(), results.get(2).object().getPosition().getZ().toString());
    
    Sensor sensor = results.get(0).object();
    assertInstanceOf(OtherSensor.class, sensor);
    assertEquals(maps.get(0).get("properties").orElseThrow(), ((OtherSensor) sensor).getProperties());
    assertEquals(maps.get(0).get("sensorType").orElseThrow(), ((OtherSensor) sensor).getSensorType());
    
    sensor = results.get(1).object();
    assertInstanceOf(AudioSensor.class, sensor);
    assertEquals(maps.get(1).get("hydrophoneId").orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
    assertEquals(maps.get(1).get("preampId").orElseThrow(), ((AudioSensor) sensor).getPreampId());
    
    sensor = results.get(2).object();
    assertInstanceOf(DepthSensor.class, sensor);
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void translateInvalidObject(Class<ObjectWithName> clazz) throws IOException, TranslatorValidationException {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of("test-uuid"),
            "name", Optional.of("test-name-1")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2")
        )
    );

    Exception result = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null)
        .map(ObjectWithRowConversionException::rowConversionException)
        .filter(Objects::nonNull)
        .map(e -> (Exception) e)
        .reduce(new Exception("Object conversions failed"), (o1, o2) -> {
          for (Throwable throwable : o2.getSuppressed()) {
            o1.addSuppressed(throwable);
          }
          return o1;
        });
    
    assertEquals(1, result.getSuppressed().length);
    Throwable cause = result.getSuppressed()[0];
    assertInstanceOf(FieldException.class, cause);
    FieldException fieldException = (FieldException) cause;
    assertEquals("invalid uuid format", fieldException.getMessage());
    assertEquals("uuid", fieldException.getProperty());
  }

  @Test
  void translateSoundSourceInvalidObject() throws IOException, TranslatorValidationException {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of("test-uuid"),
            "name", Optional.of("test-name-1"),
            "scientificName", Optional.of("test-scientific-name-1")
        ),
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-2"),
            "scientificName", Optional.of("test-scientific-name-2")
        )
    );

    Exception result = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2),
        new TestTranslatorField("scientificName", 3)
    )), maps, SoundSource.class).translate((InputStream) null)
        .map(ObjectWithRowConversionException::rowConversionException)
        .filter(Objects::nonNull)
        .map(e -> (Exception) e)
        .reduce(new Exception("Object conversions failed"), (o1, o2) -> {
          for (Throwable throwable : o2.getSuppressed()) {
            o1.addSuppressed(throwable);
          }
          return o1;
        });
    assertEquals(1, result.getSuppressed().length);
    Throwable cause = result.getSuppressed()[0];
    assertInstanceOf(FieldException.class, cause);
    FieldException fieldException = (FieldException) cause;
    assertEquals("invalid uuid format", fieldException.getMessage());
    assertEquals("uuid", fieldException.getProperty());
  }
  
  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void testInvalidTranslatorDefinition(Class<ObjectWithName> clazz) {
    TranslatorValidationException exception = assertThrows(TranslatorValidationException.class, () -> createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name-invalid", 2)
    )), Collections.emptyList(), clazz));
    assertEquals(String.format(
        "Translator does not fully describe %s. Missing fields: [name]", clazz.getSimpleName()
    ), exception.getMessage());
  }

  @Test
  void testInvalidSoundSourceTranslatorDefinition() {
    TranslatorValidationException exception = assertThrows(TranslatorValidationException.class, () -> createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), Collections.emptyList(), SoundSource.class));
    assertEquals(String.format(
        "Translator does not fully describe %s. Missing fields: [scientificName]", SoundSource.class.getSimpleName()
    ), exception.getMessage());
  }

  @Test
  void testInvalidSensorTranslatorDefinition() {
    List<TabularTranslationField> fields = new ArrayList<>(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2),
        new TestTranslatorField("description", 3)
    ));

    TranslatorValidationException exception = assertThrows(TranslatorValidationException.class, () -> createExecutor(new TestTranslator(fields), Collections.emptyList(), Sensor.class).translate((InputStream) null));
    assertEquals("Translator does not fully describe Sensor. Missing fields: [position.x, position.y, position.z, type]", exception.getMessage());
  }

  @Test
  void testUnsupportedSensor() {
    class TestSensor implements Sensor {

      @Override
      public Position getPosition() {
        return Position.builder().build();
      }

      @Override
      public String getDescription() {
        return "description";
      }

      @Override
      public String getName() {
        return "name";
      }

      @Override
      public UUID getUuid() {
        return UUID.randomUUID();
      }
    }
    
    List<TabularTranslationField> fields = new ArrayList<>(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2),
        new TestTranslatorField("description", 3)
    ));

    TranslatorValidationException exception = assertThrows(TranslatorValidationException.class, () -> createExecutor(new TestTranslator(fields), Collections.emptyList(), TestSensor.class).translate((InputStream) null));
    assertEquals(String.format(
        "Translation not supported for %s", TestSensor.class.getSimpleName()
    ), exception.getMessage());
  }
}