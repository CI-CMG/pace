package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Instrument;
import java.util.Set;
import java.util.function.Consumer;

public class InstrumentController extends CRUDController<Instrument, String> {

  protected InstrumentController(CRUDService<Instrument, String> service,
      Validator<Instrument> validator,
      Consumer<Set<ConstraintViolation>> onValidationError) {
    super(service, validator, onValidationError);
  }
}
