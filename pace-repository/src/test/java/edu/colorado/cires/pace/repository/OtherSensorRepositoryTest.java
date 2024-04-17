package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OtherSensorRepositoryTest extends SensorRepositoryTest {
  
  static class TestSensor implements Sensor {

    @Override
    public String getName() {
      return "name";
    }

    @Override
    public Position getPosition() {
      return Position.builder()
          .x(1f)
          .y(1f)
          .z(1f)
          .build();
    }

    @Override
    public String getDescription() {
      return "description";
    }

    @Override
    public UUID getUuid() {
      return null;
    }
  } 
  
  @Test
  void testUnsupported() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(new TestSensor()));
    assertEquals(String.format(
        "Unsupported sensor type: %s", TestSensor.class.getSimpleName()
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
    assertEquals(expected.getDescription(), expected.getDescription());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }

    assertEquals(((OtherSensor) expected).getProperties(), ((OtherSensor) actual).getProperties());
    assertEquals(((OtherSensor) expected).getSensorType(), ((OtherSensor) actual).getSensorType());
  }
}
