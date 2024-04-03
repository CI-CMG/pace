package edu.colorado.cires.pace.core.controller.validation;

import java.util.Set;

public class ValidationException extends Exception {
  
  private final Set<ConstraintViolation> violations;

  public ValidationException(Set<ConstraintViolation> violations) {
    super("Object validation failed");
    this.violations = violations;
  }

  public Set<ConstraintViolation> getViolations() {
    return violations;
  }
}
