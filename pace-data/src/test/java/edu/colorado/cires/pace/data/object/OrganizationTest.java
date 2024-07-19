package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class OrganizationTest extends ObjectWithUniqueFieldTest<Organization> {

  @Override
  protected Organization createObject() {
    return Organization.builder().build();
  }
}