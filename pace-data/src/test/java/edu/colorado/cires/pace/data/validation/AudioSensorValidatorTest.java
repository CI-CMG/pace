package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Position;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AudioSensorValidatorTest {
  
  @Test
  void testMissingPreampID() {
    ValidationException exception = assertThrows(ValidationException.class, () -> AudioSensor.builder()
        .name("test")
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .hydrophoneId("test")
        .build());
    
    assertEquals(String.format(
        "%s validation failed", AudioSensor.class.getSimpleName()
    ), exception.getMessage());
    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("preampId", violation.getProperty());
    assertEquals("preampId must not be blank", violation.getMessage());
  }

  @Test
  void testMissingHydrophoneID() {
    ValidationException exception = assertThrows(ValidationException.class, () -> AudioSensor.builder()
        .name("test")
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .preampId("test")
        .build());

    assertEquals(String.format(
        "%s validation failed", AudioSensor.class.getSimpleName()
    ), exception.getMessage());
    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(1, violations.size());
    ConstraintViolation violation = violations.iterator().next();
    assertEquals("hydrophoneId", violation.getProperty());
    assertEquals("hydrophoneId must not be blank", violation.getMessage());
  }

}
