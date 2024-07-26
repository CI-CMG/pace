package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;

class DepthSensorTest extends ObjectWithUniqueFieldTest<DepthSensor> {

  @Override
  protected DepthSensor createObject() {
    return DepthSensor.builder().build();
  }
}