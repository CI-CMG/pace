package edu.colorado.cires.pace.cli.command.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.PositionTranslator;
import java.util.UUID;

class AudioSensorCommandTest extends SensorCommandTest<AudioSensor, AudioSensorTranslator> {

  @Override
  protected void assertSensorTypeSpecificFields(AudioSensor expected, AudioSensor actual) {
    assertEquals(expected.getPreampId(), actual.getPreampId());
    assertEquals(expected.getHydrophoneId(), actual.getHydrophoneId());
  }

  @Override
  public AudioSensor createObject(String uniqueField, boolean withUUID) {
    return AudioSensor.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .description("description")
        .hydrophoneId("hydrophoneId")
        .preampId("preampId")
        .build();
  }

  @Override
  protected AudioSensor updateObject(AudioSensor original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "sensorName",
        "description",
        "hydrophoneId",
        "preampId"
    };
  }

  @Override
  protected AudioSensorTranslator createTranslator(String name) {
    return AudioSensorTranslator.builder()
        .name(name)
        .sensorUUID("UUID")
        .sensorName("sensorName")
        .description("description")
        .positionTranslator(PositionTranslator.builder()
            .x("position (X)")
            .y("position (Y)")
            .z("position (Z)")
            .build())
        .hydrophoneId("hydrophoneId")
        .preampId("preampId")
        .build();
  }

  @Override
  protected String[] objectToRow(AudioSensor object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName(),
        object.getDescription(),
        object.getHydrophoneId(),
        object.getPreampId()
    };
  }
}
