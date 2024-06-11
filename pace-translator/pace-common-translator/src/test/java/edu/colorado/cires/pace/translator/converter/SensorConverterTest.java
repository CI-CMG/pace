package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SensorConverterTest {
  
  private final Converter<SensorTranslator, Sensor> converter = new SensorConverter();
  
  @Test
  void convertInvalidSensorType() {
    Exception exception = assertThrows(TranslationException.class, () -> converter.convert(
        SensorTranslator.builder().build(),
        Map.of(),
        1,
        new RuntimeException()
    ));
    
    assertEquals("Translation not supported for SensorTranslator", exception.getMessage());
  }

  @Test
  void convertDepthSensor() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    float positionX = 1.0f;
    float positionY = 2.0f;
    float positionZ = 3.0f;
    String description = "description-value";

    Sensor sensor = converter.convert(
        DepthSensorTranslator.builder()
            .sensorUUID("sensor-uuid")
            .sensorName("sensor-name")
            .description("sensor-description")
            .positionTranslator(PositionTranslator.builder()
                .x("sensor-x")
                .y("sensor-y")
                .z("sensor-z")
                .build())
            .build(),
        Map.of(
            "sensor-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "sensor-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "sensor-description", new ValueWithColumnNumber(Optional.of(description), 3),
            "sensor-x", new ValueWithColumnNumber(Optional.of(Float.toString(positionX)), 4),
            "sensor-y", new ValueWithColumnNumber(Optional.of(Float.toString(positionY)), 5),
            "sensor-z", new ValueWithColumnNumber(Optional.of(Float.toString(positionZ)), 6)
        ),
        1,
        new RuntimeException()
    );
    
    assertInstanceOf(DepthSensor.class, sensor);
    DepthSensor depthSensor = (DepthSensor) sensor;
    assertEquals(uuid, depthSensor.getUuid());
    assertEquals(name, depthSensor.getName());
    assertEquals(description, depthSensor.getDescription());

    Position position = depthSensor.getPosition();
    assertEquals(positionX, position.getX());
    assertEquals(positionY, position.getY());
    assertEquals(positionZ, position.getZ());
  }
  
  @Test
  void convertAudioSensor() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    float positionX = 1.0f;
    float positionY = 2.0f;
    float positionZ = 3.0f;
    String description = "description-value";
    String hydrophoneId = "hydrophone-id-value";
    String preampId = "preamp-id-value";

    Sensor sensor = converter.convert(
        AudioSensorTranslator.builder()
            .sensorUUID("sensor-uuid")
            .sensorName("sensor-name")
            .description("sensor-description")
            .hydrophoneId("sensor-hydrophone-id")
            .preampId("sensor-preamp-id")
            .positionTranslator(PositionTranslator.builder()
                .x("sensor-x")
                .y("sensor-y")
                .z("sensor-z")
                .build())
            .build(),
        Map.of(
            "sensor-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "sensor-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "sensor-description", new ValueWithColumnNumber(Optional.of(description), 3),
            "sensor-x", new ValueWithColumnNumber(Optional.of(Float.toString(positionX)), 4),
            "sensor-y", new ValueWithColumnNumber(Optional.of(Float.toString(positionY)), 5),
            "sensor-z", new ValueWithColumnNumber(Optional.of(Float.toString(positionZ)), 6),
            "sensor-hydrophone-id", new ValueWithColumnNumber(Optional.of(hydrophoneId), 7),
            "sensor-preamp-id", new ValueWithColumnNumber(Optional.of(preampId), 8)
        ),
        1,
        new RuntimeException()
    );

    assertInstanceOf(AudioSensor.class, sensor);
    AudioSensor audioSensor = (AudioSensor) sensor;
    assertEquals(uuid, audioSensor.getUuid());
    assertEquals(name, audioSensor.getName());
    assertEquals(description, audioSensor.getDescription());
    assertEquals(hydrophoneId, audioSensor.getHydrophoneId());
    assertEquals(preampId, audioSensor.getPreampId());

    Position position = audioSensor.getPosition();
    assertEquals(positionX, position.getX());
    assertEquals(positionY, position.getY());
    assertEquals(positionZ, position.getZ());
  }

  @Test
  void convertOtherSensor() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    float positionX = 1.0f;
    float positionY = 2.0f;
    float positionZ = 3.0f;
    String description = "description-value";
    String sensorType = "sensor-type-value";
    String properties = "properties-value";

    Sensor sensor = converter.convert(
        OtherSensorTranslator.builder()
            .sensorUUID("sensor-uuid")
            .sensorName("sensor-name")
            .description("sensor-description")
            .sensorType("sensor-type")
            .properties("sensor-properties")
            .positionTranslator(PositionTranslator.builder()
                .x("sensor-x")
                .y("sensor-y")
                .z("sensor-z")
                .build())
            .build(),
        Map.of(
            "sensor-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "sensor-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "sensor-description", new ValueWithColumnNumber(Optional.of(description), 3),
            "sensor-x", new ValueWithColumnNumber(Optional.of(Float.toString(positionX)), 4),
            "sensor-y", new ValueWithColumnNumber(Optional.of(Float.toString(positionY)), 5),
            "sensor-z", new ValueWithColumnNumber(Optional.of(Float.toString(positionZ)), 6),
            "sensor-type", new ValueWithColumnNumber(Optional.of(sensorType), 7),
            "sensor-properties", new ValueWithColumnNumber(Optional.of(properties), 8)
        ),
        1,
        new RuntimeException()
    );

    assertInstanceOf(OtherSensor.class, sensor);
    OtherSensor otherSensor = (OtherSensor) sensor;
    assertEquals(uuid, otherSensor.getUuid());
    assertEquals(name, otherSensor.getName());
    assertEquals(description, otherSensor.getDescription());
    assertEquals(properties, otherSensor.getProperties());
    assertEquals(sensorType, otherSensor.getSensorType());

    Position position = otherSensor.getPosition();
    assertEquals(positionX, position.getX());
    assertEquals(positionY, position.getY());
    assertEquals(positionZ, position.getZ());
  }
}