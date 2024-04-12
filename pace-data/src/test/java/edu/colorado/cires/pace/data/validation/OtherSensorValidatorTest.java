package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.OtherSensor;
import edu.colorado.cires.pace.data.object.Position;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class OtherSensorValidatorTest {
  
  @Test
  void testMissingSensorType() {
    ValidationException exception = assertThrows(ValidationException.class, () -> OtherSensor.builder()
        .name("test")
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .properties("test")
        .build());
    
    assertEquals(String.format(
        "%s validation failed", OtherSensor.class.getSimpleName()
    ), exception.getMessage());
    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("sensorType", violation.getProperty());
    assertEquals("sensorType must not be blank", violation.getMessage());
  }

  @Test
  void testMissingProperties() {
    ValidationException exception = assertThrows(ValidationException.class, () -> OtherSensor.builder()
        .name("test")
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .sensorType("test")
        .build());

    assertEquals(String.format(
        "%s validation failed", OtherSensor.class.getSimpleName()
    ), exception.getMessage());
    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("properties", violation.getProperty());
    assertEquals("properties must not be blank", violation.getMessage());
  }

}
