package edu.colorado.cires.pace.data.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import org.junit.jupiter.api.Test;

abstract class SingularRequiredFieldValidatorTest<O extends ObjectWithUniqueField> {
  
  protected abstract BaseValidator<O> createValidator();
  protected abstract O createObject(String uniqueField) throws ValidationException;
  protected abstract String getUniqueFieldName();
  protected abstract String getClassName();
  
  @Test
  void testInvalid() {
    ValidationException validationException = assertThrows(ValidationException.class, () -> createValidator().validate(createObject(null)));
    assertEquals(String.format(
        "%s validation failed", getClassName()
    ), validationException.getMessage());
    assertEquals(1, validationException.getViolations().size());
    ConstraintViolation violation = validationException.getViolations().iterator().next();
    assertEquals(getUniqueFieldName(), violation.getProperty());
    assertEquals(String.format(
        "%s must not be blank", getUniqueFieldName()
    ), violation.getMessage());
  }
  
  @Test
  void testValid() {
    assertDoesNotThrow(() -> createValidator().validate(createObject("test")));
  }

}
