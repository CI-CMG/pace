package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;

class OtherSensorRepositoryTest extends SensorRepositoryTest {

  @Override
  protected Sensor createNewObject(int suffix) {
    
    return OtherSensor.builder()
        .description(String.format("description-%s", suffix))
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .name(String.format("name-%s", suffix))
        .properties(String.format("properties-%s", suffix))
        .sensorType(String.format("sensorType-%s", suffix))
        .build();
  }

  @Override
  protected Sensor copyWithUpdatedUniqueField(Sensor object, String uniqueField) {
    return ((OtherSensor) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Sensor expected, Sensor actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getDescription(), actual.getDescription());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }

    assertEquals(((OtherSensor) expected).getProperties(), ((OtherSensor) actual).getProperties());
    assertEquals(((OtherSensor) expected).getSensorType(), ((OtherSensor) actual).getSensorType());
  }
}
