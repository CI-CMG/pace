package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Platform;

public class PlatformValidatorTest extends SingularRequiredFieldValidatorTest<Platform> {

  @Override
  protected BaseValidator<Platform> createValidator() {
    return new PlatformValidator();
  }

  @Override
  protected Platform createObject(String uniqueField) throws ValidationException {
    return Platform.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return Platform.class.getSimpleName();
  }
}
