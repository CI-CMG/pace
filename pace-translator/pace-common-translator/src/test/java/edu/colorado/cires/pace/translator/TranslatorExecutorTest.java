package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.object.ObjectWithName;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
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
    private final Integer columnNumber;

    TestTranslatorField(String propertyName, Integer columnNumber) {
      this.propertyName = propertyName;
      this.columnNumber = columnNumber;
    }

    @Override
    public String getPropertyName() {
      return propertyName;
    }

    @Override
    public Integer getColumnNumber() {
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
  
  private <O> TranslatorExecutor<O, TabularTranslator<TabularTranslationField>> createExecutor(TestTranslator translator, List<Map<String, ValueWithColumnNumber>> maps, Class<O> clazz) {
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
  void translateFromInputStream(Class<ObjectWithName> clazz) throws IOException {
    List<Map<String, ValueWithColumnNumber>> maps = List.of(
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-1"), 1)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-2"), 1)
        )
    );
    
    List<ObjectWithRowException<ObjectWithName>> results = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null).toList();
    assertEquals(2, results.size());
    assertEquals(maps.get(0).get("uuid").value().orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).throwable());
    assertEquals(maps.get(0).get("name").value().orElseThrow(), results.get(0).object().getName());
    assertEquals(maps.get(1).get("uuid").value().orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).throwable());
    assertEquals(maps.get(1).get("name").value().orElseThrow(), results.get(1).object().getName());
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void translateFromReader(Class<ObjectWithName> clazz) throws IOException {
    List<Map<String, ValueWithColumnNumber>> maps = List.of(
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-1"), 1)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-2"), 1)
        )
    );

    List<ObjectWithRowException<ObjectWithName>> results = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((Reader) null).toList();
    assertEquals(2, results.size());
    assertEquals(maps.get(0).get("uuid").value().orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).throwable());
    assertEquals(maps.get(0).get("name").value().orElseThrow(), results.get(0).object().getName());
    assertEquals(maps.get(1).get("uuid").value().orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).throwable());
    assertEquals(maps.get(1).get("name").value().orElseThrow(), results.get(1).object().getName());
  }

  @Test
  void translateSoundSource() throws IOException {
    List<Map<String, ValueWithColumnNumber>> maps = List.of(
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "source", new ValueWithColumnNumber(Optional.of("test-name-1"), 1),
            "scienceName", new ValueWithColumnNumber(Optional.of("test-scientific-name-1"), 2)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "source", new ValueWithColumnNumber(Optional.of("test-name-2"), 1),
            "scienceName", new ValueWithColumnNumber(Optional.of("test-scientific-name-2"), 2)
        )
    );

    List<ObjectWithRowException<DetectionType>> results = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("source", 2),
        new TestTranslatorField("scienceName", 3)
    )), maps, DetectionType.class).translate((InputStream) null).toList();
    assertEquals(2, results.size());
    assertEquals(maps.get(0).get("uuid").value().orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).throwable());
    assertEquals(maps.get(0).get("source").value().orElseThrow(), results.get(0).object().getSource());
    assertEquals(maps.get(0).get("scienceName").value().orElseThrow(), results.get(0).object().getScienceName());
    assertEquals(maps.get(1).get("uuid").value().orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).throwable());
    assertEquals(maps.get(1).get("source").value().orElseThrow(), results.get(1).object().getSource());
    assertEquals(maps.get(1).get("scienceName").value().orElseThrow(), results.get(1).object().getScienceName());
  }

  @Test
  void translateSensor() throws IOException {
    List<Map<String, ValueWithColumnNumber>> maps = List.of(
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-1"), 0),
            "description", new ValueWithColumnNumber(Optional.of("test-description-1"), 0),
            "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 0),
            "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 0),
            "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 0),
            "properties", new ValueWithColumnNumber(Optional.of("test-properties-1"), 0),
            "sensorType", new ValueWithColumnNumber(Optional.of("test-sensor-type-1"), 0),
            "type", new ValueWithColumnNumber(Optional.of("other"), 0)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-2"), 0),
            "description", new ValueWithColumnNumber(Optional.of("test-description-2"), 0),
            "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 0),
            "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 0),
            "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 0),
            "hydrophoneId", new ValueWithColumnNumber(Optional.of("test-hydrophoneId-2"), 0),
            "preampId", new ValueWithColumnNumber(Optional.of("test-preampId-2"), 0),
            "type", new ValueWithColumnNumber(Optional.of("audio"), 0)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-3"), 0),
            "description", new ValueWithColumnNumber(Optional.of("test-description-3"), 0),
            "position.x", new ValueWithColumnNumber(Optional.of("1.0"), 0),
            "position.y", new ValueWithColumnNumber(Optional.of("2.0"), 0),
            "position.z", new ValueWithColumnNumber(Optional.of("3.0"), 0),
            "type", new ValueWithColumnNumber(Optional.of("depth"), 0)
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

    List<ObjectWithRowException<Sensor>> results = createExecutor(new TestTranslator(fields), maps, Sensor.class).translate((InputStream) null).toList();
    assertEquals(3, results.size());
    assertEquals(maps.get(0).get("uuid").value().orElseThrow(), results.get(0).object().getUuid().toString());
    assertNull(results.get(0).throwable());
    assertEquals(maps.get(0).get("name").value().orElseThrow(), results.get(0).object().getName());
    assertEquals(maps.get(0).get("description").value().orElseThrow(), results.get(0).object().getDescription());
    assertEquals(maps.get(0).get("position.x").value().orElseThrow(), results.get(0).object().getPosition().getX().toString());
    assertEquals(maps.get(0).get("position.y").value().orElseThrow(), results.get(0).object().getPosition().getY().toString());
    assertEquals(maps.get(0).get("position.z").value().orElseThrow(), results.get(0).object().getPosition().getZ().toString());
    assertEquals(maps.get(1).get("uuid").value().orElseThrow(), results.get(1).object().getUuid().toString());
    assertNull(results.get(1).throwable());
    assertEquals(maps.get(1).get("name").value().orElseThrow(), results.get(1).object().getName());
    assertEquals(maps.get(1).get("description").value().orElseThrow(), results.get(1).object().getDescription());
    assertEquals(maps.get(1).get("position.x").value().orElseThrow(), results.get(1).object().getPosition().getX().toString());
    assertEquals(maps.get(1).get("position.y").value().orElseThrow(), results.get(1).object().getPosition().getY().toString());
    assertEquals(maps.get(1).get("position.z").value().orElseThrow(), results.get(1).object().getPosition().getZ().toString());
    assertEquals(maps.get(2).get("uuid").value().orElseThrow(), results.get(2).object().getUuid().toString());
    assertNull(results.get(2).throwable());
    assertEquals(maps.get(2).get("name").value().orElseThrow(), results.get(2).object().getName());
    assertEquals(maps.get(2).get("description").value().orElseThrow(), results.get(2).object().getDescription());
    assertEquals(maps.get(2).get("position.x").value().orElseThrow(), results.get(2).object().getPosition().getX().toString());
    assertEquals(maps.get(2).get("position.y").value().orElseThrow(), results.get(2).object().getPosition().getY().toString());
    assertEquals(maps.get(2).get("position.z").value().orElseThrow(), results.get(2).object().getPosition().getZ().toString());
    
    Sensor sensor = results.get(0).object();
    assertInstanceOf(OtherSensor.class, sensor);
    assertEquals(maps.get(0).get("properties").value().orElseThrow(), ((OtherSensor) sensor).getProperties());
    assertEquals(maps.get(0).get("sensorType").value().orElseThrow(), ((OtherSensor) sensor).getSensorType());
    
    sensor = results.get(1).object();
    assertInstanceOf(AudioSensor.class, sensor);
    assertEquals(maps.get(1).get("hydrophoneId").value().orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
    assertEquals(maps.get(1).get("preampId").value().orElseThrow(), ((AudioSensor) sensor).getPreampId());
    
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
  void translateInvalidObject(Class<ObjectWithName> clazz) throws IOException {
    List<Map<String, ValueWithColumnNumber>> maps = List.of(
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-1"), 0)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "name", new ValueWithColumnNumber(Optional.of("test-name-2"), 0)
        )
    );

    Exception result = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null)
        .map(ObjectWithRowException::throwable)
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
  void translateSoundSourceInvalidObject() throws IOException {
    List<Map<String, ValueWithColumnNumber>> maps = List.of(
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of("test-uuid"), 0),
            "source", new ValueWithColumnNumber(Optional.of("test-name-1"), 0),
            "scienceName", new ValueWithColumnNumber(Optional.of("test-scientific-name-1"), 0)
        ),
        Map.of(
            "uuid", new ValueWithColumnNumber(Optional.of(UUID.randomUUID().toString()), 0),
            "source", new ValueWithColumnNumber(Optional.of("test-name-2"), 0),
            "scienceName", new ValueWithColumnNumber(Optional.of("test-scientific-name-2"), 0)
        )
    );

    Exception result = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("source", 2),
        new TestTranslatorField("scienceName", 3)
    )), maps, DetectionType.class).translate((InputStream) null)
        .map(ObjectWithRowException::throwable)
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
}