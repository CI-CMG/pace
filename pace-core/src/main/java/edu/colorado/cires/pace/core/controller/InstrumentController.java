package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Instrument;

public class InstrumentController extends CRUDController<Instrument, String> {

  public InstrumentController(CRUDService<Instrument, String> service,
      Validator<Instrument> validator) {
    super(service, validator);
  }
}
