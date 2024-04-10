package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Sea;

class SeaValidatorTest extends SingularRequiredFieldValidatorTest<Sea> {

  @Override
  protected BaseValidator<Sea> createValidator() {
    return new SeaValidator();
  }

  @Override
  protected Sea createObject(String uniqueField) {
    return Sea.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return Sea.class.getSimpleName();
  }
}
