package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.OrganizationRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.OrganizationService;
import edu.colorado.cires.pace.core.validation.OrganizationValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Organization;

public class OrganizationController extends CRUDController<Organization, String> {

  @Override
  protected Validator<Organization> getValidator() {
    return new OrganizationValidator();
  }

  @Override
  protected CRUDService<Organization, String> createService(Datastore<Organization, String> datastore, Datastore<?, ?>... additionalDataStores) {
    return new OrganizationService(
        new OrganizationRepository(
            datastore
        )
    );
  }

  public OrganizationController(Datastore<Organization, String> datastore) {
    super(datastore);
  }
}