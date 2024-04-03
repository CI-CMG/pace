package edu.colorado.cires.pace.core.controller.validation;

import java.util.Set;

@FunctionalInterface
public interface Validator<O> {
  
  Set<ConstraintViolation> validate(O object); 

}
