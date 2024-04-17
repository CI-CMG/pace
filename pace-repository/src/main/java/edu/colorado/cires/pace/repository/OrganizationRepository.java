package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class OrganizationRepository extends CRUDRepository<Organization> {

  public OrganizationRepository(Datastore<Organization> datastore) {
    super(datastore);
  }

  @Override
  protected Organization setUUID(Organization object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
