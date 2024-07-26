package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import java.util.UUID;

class OrganizationFormTest extends ContactFormTest<Organization, OrganizationForm> {

  @Override
  protected OrganizationForm createMetadataForm(Organization initialObject) {
    return new OrganizationForm(initialObject);
  }

  @Override
  protected Organization createObject() {
    return Organization.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .visible(true)
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .build();
  }
}