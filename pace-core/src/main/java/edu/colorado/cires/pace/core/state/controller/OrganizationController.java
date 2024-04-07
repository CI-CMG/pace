package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.validation.OrganizationValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Organization;

public class OrganizationController extends CRUDController<Organization, String> {

  @Override
  protected Validator<Organization> getValidator() {
    return new OrganizationValidator();
  }

  public OrganizationController(CRUDService<Organization, String> service) {
    super(service);
  }
}
