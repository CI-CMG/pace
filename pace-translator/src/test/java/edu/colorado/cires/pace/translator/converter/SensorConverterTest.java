package edu.colorado.cires.pace.translator.converter;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.other.OtherSensor;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;
import edu.colorado.cires.pace.data.object.sensor.audio.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.object.sensor.depth.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.object.sensor.other.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;
import edu.colorado.cires.pace.translator.TranslationException;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
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
    String description = "description-value";
    String sensorId = "sensor-id";

    Sensor sensor = converter.convert(
        DepthSensorTranslator.builder()
            .sensorUUID("sensor-uuid")
            .sensorName("sensor-name")
            .description("sensor-description")
            .id("sensor-id")
            .build(),
        Map.of(
            "sensor-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "sensor-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "sensor-description", new ValueWithColumnNumber(Optional.of(description), 3),
            "sensor-id", new ValueWithColumnNumber(Optional.of(sensorId), 4)
        ),
        1,
        new RuntimeException()
    );
    
    assertInstanceOf(DepthSensor.class, sensor);
    DepthSensor depthSensor = (DepthSensor) sensor;
    assertEquals(uuid, depthSensor.getUuid());
    assertEquals(name, depthSensor.getName());
    assertEquals(description, depthSensor.getDescription());
    assertEquals(sensorId, depthSensor.getId());
  }
  
  @Test
  void convertAudioSensor() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    String description = "description-value";
    String hydrophoneId = "hydrophone-id-value";
    String preampId = "preamp-id-value";
    String sensorId = "sensor-id";

    Sensor sensor = converter.convert(
        AudioSensorTranslator.builder()
            .sensorUUID("sensor-uuid")
            .sensorName("sensor-name")
            .description("sensor-description")
            .hydrophoneId("sensor-hydrophone-id")
            .preampId("sensor-preamp-id")
            .id("sensor-id")
            .build(),
        Map.of(
            "sensor-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "sensor-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "sensor-description", new ValueWithColumnNumber(Optional.of(description), 3),
            "sensor-hydrophone-id", new ValueWithColumnNumber(Optional.of(hydrophoneId), 7),
            "sensor-preamp-id", new ValueWithColumnNumber(Optional.of(preampId), 8),
            "sensor-id", new ValueWithColumnNumber(Optional.of(sensorId), 9)
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
    assertEquals(sensorId, audioSensor.getId());
  }

  @Test
  void convertOtherSensor() throws TranslationException {
    UUID uuid = UUID.randomUUID();
    String name = "name-value";
    String description = "description-value";
    String sensorType = "sensor-type-value";
    String properties = "properties-value";
    String sensorId = "sensor-id";

    Sensor sensor = converter.convert(
        OtherSensorTranslator.builder()
            .sensorUUID("sensor-uuid")
            .sensorName("sensor-name")
            .description("sensor-description")
            .sensorType("sensor-type")
            .properties("sensor-properties")
            .id("sensor-id")
            .build(),
        Map.of(
            "sensor-uuid", new ValueWithColumnNumber(Optional.of(uuid.toString()), 1),
            "sensor-name", new ValueWithColumnNumber(Optional.of(name), 2),
            "sensor-description", new ValueWithColumnNumber(Optional.of(description), 3),
            "sensor-type", new ValueWithColumnNumber(Optional.of(sensorType), 7),
            "sensor-properties", new ValueWithColumnNumber(Optional.of(properties), 8),
            "sensor-id", new ValueWithColumnNumber(Optional.of(sensorId), 9)
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
  }
}