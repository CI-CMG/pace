package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Organization;

public class OrganizationService extends CRUDService<Organization> {

  public OrganizationService(CRUDRepository<Organization> organizationRepository) {
    super(organizationRepository);
  }
}
