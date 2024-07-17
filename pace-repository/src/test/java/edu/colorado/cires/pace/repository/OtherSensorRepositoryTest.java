package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import org.junit.jupiter.api.Test;

class OtherSensorRepositoryTest extends SensorRepositoryTest {
  
  private static final Sensor sensor = new Sensor(
      DepthSensor.builder()
          .name("name")
          .position(Position.builder()
              .x(1f)
              .y(1f)
              .z(1f)
              .build())
          .description("description")
  ) {};
  
  @Test
  void testUnsupported() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(sensor));
    assertEquals(String.format(
        "Unsupported sensor type: %s", sensor.getClass().getSimpleName()
    ), exception.getMessage());
  }

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
