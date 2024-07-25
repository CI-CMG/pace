package edu.colorado.cires.pace.cli.command.sensor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import edu.colorado.cires.pace.data.translator.SensorTranslator;
import java.util.List;

abstract class SensorCommandTest<S extends Sensor, T extends SensorTranslator> extends TranslateCommandTest<S, T> {

  @Override
  protected TypeReference<List<S>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<S> getClazz() {
    return (Class<S>) Sensor.class;
  }

  @Override
  protected String getRepositoryDirectory() {
    return "sensors";
  }

  @Override
  protected String getCommandPrefix() {
    return "sensor";
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getUniqueField(S object) {
    return object.getName();
  }

  @Override
  protected void assertObjectsEqual(S expected, S actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());

    Position expectedPosition = expected.getPosition();
    Position actualPosition = actual.getPosition();
    assertEquals(expectedPosition.getX(), actualPosition.getX());
    assertEquals(expectedPosition.getY(), actualPosition.getY());
    assertEquals(expectedPosition.getZ(), actualPosition.getZ());

    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
    
    assertSensorTypeSpecificFields(expected, actual);
  }
  
  protected abstract void assertSensorTypeSpecificFields(S expected, S actual);
}