package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Organization;
import java.util.UUID;
import java.util.function.Supplier;

class OrganizationControllerTest extends CRUDControllerTest<Organization, String> {

  @Override
  protected CRUDController<Organization, String> createController(Datastore<Organization, String> datastore) {
    return new OrganizationController(datastore);
  }

  @Override
  protected UniqueFieldProvider<Organization, String> getUniqueFieldProvider() {
    return Organization::getName;
  }

  @Override
  protected UUIDProvider<Organization> getUuidProvider() {
    return Organization::getUUID;
  }

  @Override
  protected UniqueFieldSetter<Organization, String> getUniqueFieldSetter() {
    return Organization::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Organization createNewObject(boolean withUUID) {
    Organization organization = new Organization();
    organization.setCity(UUID.randomUUID().toString());
    organization.setCountry(UUID.randomUUID().toString());
    organization.setEmail(UUID.randomUUID().toString());
    organization.setName(UUID.randomUUID().toString());
    organization.setPhone(UUID.randomUUID().toString());
    organization.setState(UUID.randomUUID().toString());
    organization.setStreet(UUID.randomUUID().toString());
    organization.setUse(true);
    if (withUUID) {
      organization.setUUID(UUID.randomUUID());
    }
    organization.setZip(UUID.randomUUID().toString());
    return organization;
  }
}
