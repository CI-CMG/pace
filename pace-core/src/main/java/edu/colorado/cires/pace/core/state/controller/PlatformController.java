package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.validation.PlatformValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Platform;

public class PlatformController extends CRUDController<Platform, String> {

  @Override
  protected Validator<Platform> getValidator() {
    return new PlatformValidator();
  }

  public PlatformController(CRUDService<Platform, String> service) {
    super(service);
  }
}
