package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.ConstraintViolation;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

/**
 * ConstraintViolationFactory creates a checker for constraint violations
 */
final class ConstraintViolationFactory {

  /**
   * Creates a constraint violation checker
   * @param message interpolated message
   * @param path string path to property
   * @return ConstraintViolation checker
   * @param <O> object type
   */
  public static <O extends AbstractObject> ConstraintViolation<O> create(String message, String path) {
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
