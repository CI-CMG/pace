package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.DepthSensor;
import edu.colorado.cires.pace.data.object.Position;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class DepthSensorValidatorTest {

  @Test
  void testValidObject() {
    assertDoesNotThrow(() -> DepthSensor.builder()
        .name("test")
        .position(Position.builder()
            .x(1f)
            .y(1f)
            .z(1f)
            .build())
        .build());
  }
  
  @Test
  void testEmptyObject() {
    ValidationException exception = assertThrows(ValidationException.class, () -> DepthSensor.builder().build());
    assertEquals(String.format(
        "%s validation failed", DepthSensor.class.getSimpleName()
    ), exception.getMessage());
    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(2, violations.size());
    
    ConstraintViolation constraintViolation = violations.stream()
        .filter(v -> v.getProperty().equals("name"))
        .findFirst().orElseThrow();
    assertEquals("name", constraintViolation.getProperty());
    assertEquals("name must not be blank", constraintViolation.getMessage());
    
    constraintViolation = violations.stream()
        .filter(v -> v.getProperty().equals("position"))
        .findFirst().orElseThrow();
    assertEquals("position", constraintViolation.getProperty());
    assertEquals("position must be defined", constraintViolation.getMessage());
  }
  
  @Test
  void testInvalidPosition() {
    ValidationException exception = assertThrows(ValidationException.class, () -> DepthSensor.builder()
        .name("test")
        .position(Position.builder().build())
        .build());
    
    assertEquals(String.format(
        "%s validation failed", DepthSensor.class.getSimpleName()
    ), exception.getMessage());
    Set<ConstraintViolation> violations = exception.getViolations();
    assertEquals(3, violations.size());
    
    ConstraintViolation violation = violations.stream()
        .filter(v -> v.getProperty().equals("position.x"))
        .findFirst().orElseThrow();
    assertEquals("position.x", violation.getProperty());
    assertEquals("x must be defined", violation.getMessage());
    
    violation = violations.stream()
        .filter(v -> v.getProperty().equals("position.y"))
        .findFirst().orElseThrow();
    assertEquals("position.y", violation.getProperty());
    assertEquals("y must be defined", violation.getMessage());
    
    violation = violations.stream()
        .filter(v -> v.getProperty().equals("position.z"))
        .findFirst().orElseThrow();
    assertEquals("position.z", violation.getProperty());
    assertEquals("z must be defined", violation.getMessage());
  }

}
