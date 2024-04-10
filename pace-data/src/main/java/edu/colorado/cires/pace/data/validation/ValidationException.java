package edu.colorado.cires.pace.data.validation;

import java.util.Set;

public class ValidationException extends Exception {
  
  private final Set<ConstraintViolation> violations;

  public <O> ValidationException(Class<O> clazz , Set<ConstraintViolation> violations) {
    super(String.format(
        "%s validation failed", clazz.getSimpleName()
    ));
    this.violations = violations;
  }

  public Set<ConstraintViolation> getViolations() {
    return violations;
  }
}
