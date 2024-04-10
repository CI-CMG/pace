package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Instrument;

public class InstrumentValidatorTest extends SingularRequiredFieldValidatorTest<Instrument> {

  @Override
  protected BaseValidator<Instrument> createValidator() {
    return new InstrumentValidator();
  }

  @Override
  protected Instrument createObject(String uniqueField) {
    return Instrument.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected String getClassName() {
    return Instrument.class.getSimpleName();
  }
}
