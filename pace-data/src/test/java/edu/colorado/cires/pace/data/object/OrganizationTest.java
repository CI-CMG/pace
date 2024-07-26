package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.contact.organization.Organization;

class OrganizationTest extends ObjectWithUniqueFieldTest<Organization> {

  @Override
  protected Organization createObject() {
    return Organization.builder().build();
  }
}