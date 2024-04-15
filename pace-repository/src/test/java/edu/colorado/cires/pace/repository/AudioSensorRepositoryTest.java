package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.validation.ValidationException;

class AudioSensorRepositoryTest extends SensorRepositoryTest {

  @Override
  protected Sensor createNewObject(int suffix) throws ValidationException {
    return AudioSensor.builder()
        .description(String.format("description-%s", suffix))
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .name(String.format("name-%s", suffix))
        .hydrophoneId(String.format("hydrophoneId-%s", suffix))
        .preampId(String.format("preampId-%s", suffix))
        .build();
  }

  @Override
  protected Sensor copyWithUpdatedUniqueField(Sensor object, String uniqueField) throws ValidationException {
    return ((AudioSensor) object).toBuilder()
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
    
    assertEquals(((AudioSensor) expected).getPreampId(), ((AudioSensor) actual).getPreampId());
    assertEquals(((AudioSensor) expected).getHydrophoneId(), ((AudioSensor) actual).getHydrophoneId());
  }
}
