package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.validation.ShipValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Ship;

public class ShipController extends CRUDController<Ship, String> {

  @Override
  protected Validator<Ship> getValidator() {
    return new ShipValidator();
  }

  public ShipController(CRUDService<Ship, String> service) {
    super(service);
  }
}
