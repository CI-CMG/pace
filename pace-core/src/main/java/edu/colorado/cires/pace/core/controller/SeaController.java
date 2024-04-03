package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Sea;

public class SeaController extends CRUDController<Sea, String> {

  protected SeaController(CRUDService<Sea, String> service,
      Validator<Sea> validator) {
    super(service, validator);
  }
}
