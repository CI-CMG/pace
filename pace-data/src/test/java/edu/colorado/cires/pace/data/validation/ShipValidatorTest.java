package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Ship;

public class ShipValidatorTest extends SingularRequiredFieldValidatorTest<Ship> {

  @Override
  protected BaseValidator<Ship> createValidator() {
    return new ShipValidator();
  }

  @Override
  protected Ship createObject(String uniqueField) {
    return Ship.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return Ship.class.getSimpleName();
  }
}
