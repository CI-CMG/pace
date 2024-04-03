package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Organization;
import java.util.Set;
import java.util.function.Consumer;

public class OrganizationController extends CRUDController<Organization, String> {

  protected OrganizationController(CRUDService<Organization, String> service,
      Validator<Organization> validator,
      Consumer<Set<ConstraintViolation>> onValidationError) {
    super(service, validator, onValidationError);
  }
}
