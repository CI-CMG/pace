package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.SoundSource;

public class SoundSourceValidatorTest extends SingularRequiredFieldValidatorTest<SoundSource> {

  @Override
  protected BaseValidator<SoundSource> createValidator() {
    return new SoundSourceValidator();
  }

  @Override
  protected SoundSource createObject(String uniqueField) throws ValidationException {
    return SoundSource.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return SoundSource.class.getSimpleName();
  }
}
