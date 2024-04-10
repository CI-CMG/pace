package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Organization;

public class OrganizationValidatorTest extends SingularRequiredFieldValidatorTest<Organization> {

  @Override
  protected BaseValidator<Organization> createValidator() {
    return new OrganizationValidator();
  }

  @Override
  protected Organization createObject(String uniqueField) {
    return Organization.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return Organization.class.getSimpleName();
  }
}
