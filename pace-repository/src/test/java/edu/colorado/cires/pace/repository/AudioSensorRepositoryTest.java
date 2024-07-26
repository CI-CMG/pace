package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.sensor.audio.AudioSensor;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;

class AudioSensorRepositoryTest extends SensorRepositoryTest {

  @Override
  protected Sensor createNewObject(int suffix) {
    return AudioSensor.builder()
        .description(String.format("description-%s", suffix))
        .name(String.format("name-%s", suffix))
        .hydrophoneId(String.format("hydrophoneId-%s", suffix))
        .preampId(String.format("preampId-%s", suffix))
        .build();
  }

  @Override
  protected Sensor copyWithUpdatedUniqueField(Sensor object, String uniqueField) {
    return ((AudioSensor) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Sensor expected, Sensor actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    
    assertEquals(((AudioSensor) expected).getPreampId(), ((AudioSensor) actual).getPreampId());
    assertEquals(((AudioSensor) expected).getHydrophoneId(), ((AudioSensor) actual).getHydrophoneId());
  }
}
