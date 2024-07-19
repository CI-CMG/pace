package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class AudioSensorTest extends ObjectWithUniqueFieldTest<AudioSensor> {

  @Override
  protected AudioSensor createObject() {
    return AudioSensor.builder().build();
  }
}