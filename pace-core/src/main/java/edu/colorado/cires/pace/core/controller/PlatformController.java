package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Platform;

public class PlatformController extends CRUDController<Platform, String> {

  public PlatformController(CRUDService<Platform, String> service,
      Validator<Platform> validator) {
    super(service, validator);
  }
}
