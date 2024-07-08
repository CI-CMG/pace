package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.OtherSensorTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;

class OtherSensorTranslatorCommandTest extends SensorTranslatorCommandTest<OtherSensorTranslator> {

  @Override
  protected void assertSensorTranslatorTypeSpecificFields(OtherSensorTranslator expected, OtherSensorTranslator actual) {
    assertEquals(expected.getSensorType(), actual.getSensorType());
    assertEquals(expected.getProperties(), actual.getProperties());
  }

  @Override
  protected OtherSensorTranslator addSensorTypeSpecificFields(SensorTranslator sensorTranslator) {
    return OtherSensorTranslator.toBuilder(sensorTranslator)
        .sensorType("sensorType")
        .properties("properties")
        .build();
  }

  @Override
  protected OtherSensorTranslator updateObject(OtherSensorTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
