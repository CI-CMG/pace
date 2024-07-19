package edu.colorado.cires.pace.data.object;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class OtherSensorTest extends ObjectWithUniqueFieldTest<OtherSensor> {

  @Override
  protected OtherSensor createObject() {
    return OtherSensor.builder().build();
  }
}