package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.sensor.depth.DepthSensor;
import edu.colorado.cires.pace.data.object.sensor.base.Sensor;

class DepthSensorRepositoryTest extends SensorRepositoryTest {

  @Override
  protected Sensor createNewObject(int suffix) {
    return DepthSensor.builder()
        .description(String.format("description-%s", suffix))
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Sensor copyWithUpdatedUniqueField(Sensor object, String uniqueField) {
    return ((DepthSensor) object).toBuilder()
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
  }
}
