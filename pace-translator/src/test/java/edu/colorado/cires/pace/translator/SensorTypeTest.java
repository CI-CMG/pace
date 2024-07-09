package edu.colorado.cires.pace.translator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Sensor;
import org.junit.jupiter.api.Test;

class SensorTypeTest {

  @Test
  void fromSensor() {
    
    assertEquals(SensorType.audio, SensorType.fromSensor(mock(AudioSensor.class)));
    assertEquals(SensorType.depth, SensorType.fromSensor(mock(DepthSensor.class)));
    assertEquals(SensorType.other, SensorType.fromSensor(mock(OtherSensor.class)));
    assertNull(SensorType.fromSensor(mock(Sensor.class)));
  }
}