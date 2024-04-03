package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Organization;
import java.util.function.Consumer;

public class OrganizationService extends CRUDService<Organization, String> {

  protected OrganizationService(CRUDRepository<Organization, String> organizationRepository,
      Consumer<Organization> onSuccess, Consumer<Exception> onFailure) {
    super(organizationRepository, onSuccess, onFailure);
  }
}
