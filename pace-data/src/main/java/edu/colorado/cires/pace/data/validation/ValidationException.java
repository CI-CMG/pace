package edu.colorado.cires.pace.data.validation;

import java.util.Set;
import lombok.Getter;

@Getter
public class ValidationException extends Exception {
  
  private final Set<ConstraintViolation> violations;

  public <O> ValidationException(Class<O> clazz , Set<ConstraintViolation> violations) {
    super(String.format(
        "%s validation failed", clazz.getSimpleName()
    ));
    this.violations = violations;
  }

}
