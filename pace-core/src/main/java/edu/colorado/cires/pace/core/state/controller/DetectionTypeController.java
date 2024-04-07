package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.validation.DetectionTypeValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeController extends CRUDController<DetectionType, String> {

  public DetectionTypeController(CRUDService<DetectionType, String> service) {
    super(service);
  }

  @Override
  protected Validator<DetectionType> getValidator() {
    return new DetectionTypeValidator();
  }
}
