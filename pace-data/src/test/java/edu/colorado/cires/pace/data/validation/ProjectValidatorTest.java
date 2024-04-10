package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Project;

public class ProjectValidatorTest extends SingularRequiredFieldValidatorTest<Project> {

  @Override
  protected BaseValidator<Project> createValidator() {
    return new ProjectValidator();
  }

  @Override
  protected Project createObject(String uniqueField) {
    return Project.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return Project.class.getSimpleName();
  }
}
