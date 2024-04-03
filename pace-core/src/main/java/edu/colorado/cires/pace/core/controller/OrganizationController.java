package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Organization;

public class OrganizationController extends CRUDController<Organization, String> {

  protected OrganizationController(CRUDService<Organization, String> service,
      Validator<Organization> validator) {
    super(service, validator);
  }
}
