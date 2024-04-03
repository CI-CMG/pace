package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Ship;

public class ShipController extends CRUDController<Ship, String> {

  protected ShipController(CRUDService<Ship, String> service,
      Validator<Ship> validator) {
    super(service, validator);
  }
}
