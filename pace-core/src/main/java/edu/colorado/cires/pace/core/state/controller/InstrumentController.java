package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.validation.InstrumentValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentController extends CRUDController<Instrument, String> {

  public InstrumentController(CRUDService<Instrument, String> service) {
    super(service);
  }

  @Override
  protected Validator<Instrument> getValidator() {
    return new InstrumentValidator();
  }
}
