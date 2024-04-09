package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Organization;

public class OrganizationRepository extends CRUDRepository<Organization> {

  public OrganizationRepository(Datastore<Organization> datastore) {
    super(datastore);
  }

  @Override
  protected String getObjectName() {
    return Organization.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
