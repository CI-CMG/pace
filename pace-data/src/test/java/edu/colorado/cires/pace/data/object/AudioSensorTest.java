package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;

class AudioSensorTest extends ObjectWithUniqueFieldTest<AudioSensor> {

  @Override
  protected AudioSensor createObject() {
    return AudioSensor.builder().build();
  }
}