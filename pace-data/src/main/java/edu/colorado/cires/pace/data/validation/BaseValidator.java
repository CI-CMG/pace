package edu.colorado.cires.pace.data.validation;

import java.util.Set;

abstract class BaseValidator<O> {
  
  protected abstract Set<ConstraintViolation> runValidation(O object);
  
  public void run(O object) throws ValidationException {
    Set<ConstraintViolation> violations = runValidation(object);
    if (!violations.isEmpty()) {
      throw new ValidationException(
          object.getClass(),
          violations
      );
    }
  }

}
