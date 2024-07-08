package edu.colorado.cires.pace.cli.command.translator;

import edu.colorado.cires.pace.data.translator.DepthSensorTranslator;
import edu.colorado.cires.pace.data.translator.SensorTranslator;

class DepthSensorTranslatorCommandTest extends SensorTranslatorCommandTest<DepthSensorTranslator> {

  @Override
  protected void assertSensorTranslatorTypeSpecificFields(DepthSensorTranslator expected, DepthSensorTranslator actual) {
  }

  @Override
  protected DepthSensorTranslator addSensorTypeSpecificFields(SensorTranslator sensorTranslator) {
    return DepthSensorTranslator.toBuilder(sensorTranslator).build();
  }

  @Override
  protected DepthSensorTranslator updateObject(DepthSensorTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
