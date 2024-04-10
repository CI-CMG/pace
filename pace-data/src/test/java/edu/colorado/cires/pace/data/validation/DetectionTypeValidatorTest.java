package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.DetectionType;

public class DetectionTypeValidatorTest extends SingularRequiredFieldValidatorTest<DetectionType> {

  @Override
  protected BaseValidator<DetectionType> createValidator() {
    return new DetectionTypeValidator();
  }

  @Override
  protected DetectionType createObject(String uniqueField) throws ValidationException {
    return DetectionType.builder()
        .scienceName(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "scienceName";
  }

  @Override
  protected String getClassName() {
    return DetectionType.class.getSimpleName();
  }
}
