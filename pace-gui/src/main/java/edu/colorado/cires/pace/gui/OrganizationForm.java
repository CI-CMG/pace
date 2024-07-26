package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import java.util.UUID;

public class OrganizationForm extends ContactForm<Organization> {

  @Override
  protected Organization contactFromFormFields(UUID uuid, String name, boolean visible, String street, String city, String state, String zip,
      String country, String email, String phone) {
    return Organization.builder()
        .uuid(uuid)
        .name(name)
        .visible(visible)
        .street(street)
        .city(city)
        .state(state)
        .zip(zip)
        .country(country)
        .email(email)
        .phone(phone)
        .build();
  }

  public OrganizationForm(Organization initialObject) {
    super(initialObject);
  }
}
