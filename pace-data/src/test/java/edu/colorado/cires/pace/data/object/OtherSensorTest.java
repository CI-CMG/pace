package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.sensor.other.OtherSensor;

class OtherSensorTest extends ObjectWithUniqueFieldTest<OtherSensor> {

  @Override
  protected OtherSensor createObject() {
    return OtherSensor.builder().build();
  }
}