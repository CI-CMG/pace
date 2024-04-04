package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeController extends CRUDController<DetectionType, String> {

  public DetectionTypeController(CRUDService<DetectionType, String> service,
      Validator<DetectionType> validator) {
    super(service, validator);
  }
}
