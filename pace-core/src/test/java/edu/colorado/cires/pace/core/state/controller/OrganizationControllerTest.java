package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Organization;
import java.util.UUID;
import java.util.function.Supplier;

class OrganizationControllerTest extends CRUDControllerTest<Organization> {

  @Override
  protected CRUDController<Organization> createController(Datastore<Organization> datastore) {
    return new OrganizationController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Organization createNewObject(boolean withUUID) {
    return new Organization(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected Organization setUniqueField(Organization object, String uniqueField) {
    return new Organization(
        object.uuid(),
        uniqueField,
        object.street(),
        object.city(),
        object.state(),
        object.zip(),
        object.country(),
        object.email(),
        object.phone()
    );
  }
}
