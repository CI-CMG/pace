package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.FileType;

public class FileTypeValidatorTest extends SingularRequiredFieldValidatorTest<FileType> {

  @Override
  protected BaseValidator<FileType> createValidator() {
    return new FileTypeValidator();
  }

  @Override
  protected FileType createObject(String uniqueField) {
    return FileType.builder()
        .type(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "type";
  }

  @Override
  protected String getClassName() {
    return FileType.class.getSimpleName();
  }
}
