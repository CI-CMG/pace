package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class SensorTranslatorTest extends ObjectWithUniqueFieldTest<SensorTranslator> {

  @Override
  protected SensorTranslator createObject() {
    return SensorTranslator.builder().build();
  }
}