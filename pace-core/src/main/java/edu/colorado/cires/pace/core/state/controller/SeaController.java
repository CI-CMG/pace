package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.validation.SeaValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Sea;

public class SeaController extends CRUDController<Sea, String> {

  @Override
  protected Validator<Sea> getValidator() {
    return new SeaValidator();
  }

  public SeaController(CRUDService<Sea, String> service) {
    super(service);
  }
}
