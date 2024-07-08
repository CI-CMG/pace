package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.AudioSensorTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;

class AudioSensorTranslatorCommandTest extends SensorTranslatorCommandTest<AudioSensorTranslator> {

  @Override
  protected void assertSensorTranslatorTypeSpecificFields(AudioSensorTranslator expected, AudioSensorTranslator actual) {
    assertEquals(expected.getPreampId(), actual.getPreampId());
    assertEquals(expected.getHydrophoneId(), actual.getHydrophoneId());
  }

  @Override
  protected AudioSensorTranslator addSensorTypeSpecificFields(SensorTranslator sensorTranslator) {
    return AudioSensorTranslator.toBuilder(sensorTranslator)
        .preampId("preampId")
        .hydrophoneId("hydrophoneId")
        .build();
  }

  @Override
  protected AudioSensorTranslator updateObject(AudioSensorTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
