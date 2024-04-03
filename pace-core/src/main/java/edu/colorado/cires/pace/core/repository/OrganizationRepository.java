package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.Organization;

public abstract class OrganizationRepository extends CRUDRepository<Organization, String> {

  protected OrganizationRepository() {
    super(Organization::getUUID, Organization::getName, Organization::setUUID);
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
