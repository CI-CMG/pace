package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

final class ConstraintViolationFactory {
  
  public static <O extends ObjectWithUniqueField> ConstraintViolation<O> create(String message, String path) {
    return ConstraintViolationImpl.forBeanValidation(
        "",
        null,
        null,
        message,
        null,
        null,
        null,
        null,
        PathImpl.createPathFromString(path),
        null,
        null
    );
  }
}
