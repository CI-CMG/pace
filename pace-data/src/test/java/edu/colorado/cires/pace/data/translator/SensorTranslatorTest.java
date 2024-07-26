package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.sensor.base.translator.SensorTranslator;

class SensorTranslatorTest extends ObjectWithUniqueFieldTest<SensorTranslator> {

  @Override
  protected SensorTranslator createObject() {
    return SensorTranslator.builder().build();
  }
}