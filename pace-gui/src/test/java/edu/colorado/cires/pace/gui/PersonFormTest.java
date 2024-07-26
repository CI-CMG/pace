package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import java.util.UUID;
import javax.swing.JPanel;

class PersonFormTest extends ContactFormTest<Person, PersonForm> {

  @Override
  protected PersonForm createMetadataForm(Person initialObject) {
    return new PersonForm(initialObject);
  }

  @Override
  protected Person createObject() {
    return Person.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .visible(true)
        .organization("organization")
        .position("position")
        .orcid("orcid")
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .build();
  }

  @Override
  protected void populateAdditionalFormFields(Person object, JPanel contentPanel) {
    super.populateAdditionalFormFields(object, contentPanel);
    updateTextField(contentPanel, "orcid", object.getOrcid());
    updateTextField(contentPanel, "position", object.getPosition());
    updateTextField(contentPanel, "organization", object.getOrganization());
  }

  @Override
  protected void assertAdditionalFieldsEqual(Person expected, Person actual) {
    super.assertAdditionalFieldsEqual(expected, actual);
    assertEquals(expected.getOrcid(), actual.getOrcid());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getOrganization(), actual.getOrganization());
  }
}
