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
import edu.colorado.cires.pace.data.object.TabularTranslationField;
import edu.colorado.cires.pace.data.object.TabularTranslator;
import edu.colorado.cires.pace.data.validation.ConstraintViolation;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
      throws TranslationException {
    return new TranslatorExecutor<>(translator, clazz) {

      @Override
      protected Stream<Map<String, Optional<String>>> getPropertyStream(InputStream inputStream,
          TabularTranslator<TabularTranslationField> translatorDefinition) {
        return maps.stream();
      }

      @Override
      protected Stream<Map<String, Optional<String>>> getPropertyStream(Reader reader,
          TabularTranslator<TabularTranslationField> translatorDefinition) {
        return maps.stream();
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
  void translate(Class<ObjectWithName> clazz) throws TranslationException {
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
    
    List<ObjectWithName> ships = createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null).toList();
    assertEquals(2, ships.size());
    assertEquals(maps.get(0).get("uuid").orElseThrow(), ships.get(0).getUuid().toString());
    assertEquals(maps.get(0).get("name").orElseThrow(), ships.get(0).getName());
    assertEquals(maps.get(1).get("uuid").orElseThrow(), ships.get(1).getUuid().toString());
    assertEquals(maps.get(1).get("name").orElseThrow(), ships.get(1).getName());
  }

  @Test
  void translateSensor() throws TranslationException {
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

    List<Sensor> sensors = createExecutor(new TestTranslator(fields), maps, Sensor.class).translate((InputStream) null).toList();
    assertEquals(3, sensors.size());
    assertEquals(maps.get(0).get("uuid").orElseThrow(), sensors.get(0).getUuid().toString());
    assertEquals(maps.get(0).get("name").orElseThrow(), sensors.get(0).getName());
    assertEquals(maps.get(0).get("description").orElseThrow(), sensors.get(0).getDescription());
    assertEquals(maps.get(0).get("position.x").orElseThrow(), sensors.get(0).getPosition().getX().toString());
    assertEquals(maps.get(0).get("position.y").orElseThrow(), sensors.get(0).getPosition().getY().toString());
    assertEquals(maps.get(0).get("position.z").orElseThrow(), sensors.get(0).getPosition().getZ().toString());
    assertEquals(maps.get(1).get("uuid").orElseThrow(), sensors.get(1).getUuid().toString());
    assertEquals(maps.get(1).get("name").orElseThrow(), sensors.get(1).getName());
    assertEquals(maps.get(1).get("description").orElseThrow(), sensors.get(1).getDescription());
    assertEquals(maps.get(1).get("position.x").orElseThrow(), sensors.get(1).getPosition().getX().toString());
    assertEquals(maps.get(1).get("position.y").orElseThrow(), sensors.get(1).getPosition().getY().toString());
    assertEquals(maps.get(1).get("position.z").orElseThrow(), sensors.get(1).getPosition().getZ().toString());
    assertEquals(maps.get(2).get("uuid").orElseThrow(), sensors.get(2).getUuid().toString());
    assertEquals(maps.get(2).get("name").orElseThrow(), sensors.get(2).getName());
    assertEquals(maps.get(2).get("description").orElseThrow(), sensors.get(2).getDescription());
    assertEquals(maps.get(2).get("position.x").orElseThrow(), sensors.get(2).getPosition().getX().toString());
    assertEquals(maps.get(2).get("position.y").orElseThrow(), sensors.get(2).getPosition().getY().toString());
    assertEquals(maps.get(2).get("position.z").orElseThrow(), sensors.get(2).getPosition().getZ().toString());
    
    Sensor sensor = sensors.get(0);
    assertInstanceOf(OtherSensor.class, sensor);
    assertEquals(maps.get(0).get("properties").orElseThrow(), ((OtherSensor) sensor).getProperties());
    assertEquals(maps.get(0).get("sensorType").orElseThrow(), ((OtherSensor) sensor).getSensorType());
    
    sensor = sensors.get(1);
    assertInstanceOf(AudioSensor.class, sensor);
    assertEquals(maps.get(1).get("hydrophoneId").orElseThrow(), ((AudioSensor) sensor).getHydrophoneId());
    assertEquals(maps.get(1).get("preampId").orElseThrow(), ((AudioSensor) sensor).getPreampId());
    
    sensor = sensors.get(2);
    assertInstanceOf(DepthSensor.class, sensor);
  }

  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void translateInvalidObject(Class<ObjectWithName> clazz) {
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

    RuntimeException exception = assertThrows(RuntimeException.class, () -> createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2)
    )), maps, clazz).translate((InputStream) null).toList());
    Throwable cause = exception.getCause();
    assertInstanceOf(ValidationException.class, cause);
    ValidationException validationException = (ValidationException) cause;
    assertEquals(String.format(
        "%s validation failed", clazz.getSimpleName()
    ), validationException.getMessage());
    Set<ConstraintViolation> violations = validationException.getViolations();
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("uuid", violation.getProperty());
    assertEquals("invalid uuid format", violation.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(strings = {
      "other",
      "audio",
      "depth"
  })
  void translateInvalidSensor(String type) {
    List<Map<String, Optional<String>>> maps = List.of(
        Map.of(
            "uuid", Optional.of(UUID.randomUUID().toString()),
            "name", Optional.of("test-name-1"),
            "description", Optional.of("test-description-1"),
            "hydrophoneId", Optional.of("test-hydrophoneId-1"),
            "preampId", Optional.of("test-preampId-1"),
            "properties", Optional.of("test-properties-1"),
            "sensorType", Optional.of("test-sensor-type-1"),
            "type", Optional.of(type)
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
    
    RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> createExecutor(new TestTranslator(fields), maps, Sensor.class).translate((InputStream) null).toList());
    assertInstanceOf(ValidationException.class, runtimeException.getCause());
    ValidationException exception = (ValidationException) runtimeException.getCause(); 
    assertEquals(String.format(
        "%s validation failed", Sensor.class.getSimpleName()
    ), exception.getMessage());
    assertEquals(3, exception.getViolations().size());
    ConstraintViolation violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("position.x"))
        .findFirst().orElseThrow();
    assertEquals("position.x", violation.getProperty());
    assertEquals("x must be defined", violation.getMessage());

    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("position.y"))
        .findFirst().orElseThrow();
    assertEquals("position.y", violation.getProperty());
    assertEquals("y must be defined", violation.getMessage());

    violation = exception.getViolations().stream()
        .filter(v -> v.getProperty().equals("position.z"))
        .findFirst().orElseThrow();
    assertEquals("position.z", violation.getProperty());
    assertEquals("z must be defined", violation.getMessage());
  }
  
  @ParameterizedTest
  @ValueSource(classes = {
      Ship.class,
      Sea.class,
      Project.class,
      Platform.class
  })
  void testInvalidTranslatorDefinition(Class<ObjectWithName> clazz) {
    TranslationException exception = assertThrows(TranslationException.class, () -> createExecutor(new TestTranslator(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name-invalid", 2)
    )), Collections.emptyList(), clazz));
    assertEquals(String.format(
        "Translator does not fully describe %s. Missing fields: [name]", clazz.getSimpleName()
    ), exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "other",
      "depth",
      "audio"
  })
  void testInvalidSensorTranslatorDefinition(String type) {
    List<TabularTranslationField> fields = new ArrayList<>(List.of(
        new TestTranslatorField("uuid", 1),
        new TestTranslatorField("name", 2),
        new TestTranslatorField("description", 3)
    ));

    TranslationException exception = assertThrows(TranslationException.class, () -> createExecutor(new TestTranslator(fields), Collections.emptyList(), Sensor.class).translate((InputStream) null));
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

    TranslationException exception = assertThrows(TranslationException.class, () -> createExecutor(new TestTranslator(fields), Collections.emptyList(), TestSensor.class).translate((InputStream) null));
    assertEquals(String.format(
        "Translation not supported for %s", TestSensor.class.getSimpleName()
    ), exception.getMessage());
  }
}