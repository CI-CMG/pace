package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Platform;
import java.util.Set;
import java.util.function.Consumer;

public class PlatformController extends CRUDController<Platform, String> {

  protected PlatformController(CRUDService<Platform, String> service,
      Validator<Platform> validator,
      Consumer<Set<ConstraintViolation>> onValidationError) {
    super(service, validator, onValidationError);
  }
}
