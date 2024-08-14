package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;

import edu.colorado.cires.pace.data.object.contact.person.translator.PersonTranslator;

public class PersonTranslatorFormTest extends BaseTranslatorFormTest<PersonTranslator, PersonTranslatorForm> {

  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "UUID", "Name", "Position", "Organization", "Street", "City", "State", "Zip", "Country", "Email", "Phone", "Orcid" 
    };
  }

  @Override
  protected PersonTranslatorForm createForm(PersonTranslator personTranslator, String[] headerOptions) {
    return new PersonTranslatorForm(personTranslator, headerOptions);
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(PersonTranslator translator) {
    assertEquals("UUID", translator.getPersonUUID());
    assertEquals("Name", translator.getPersonName());
    assertEquals("Position", translator.getPosition());
    assertEquals("Organization", translator.getOrganization());
    assertEquals("Street", translator.getStreet());
    assertEquals("City", translator.getCity());
    assertEquals("State", translator.getState());
    assertEquals("Zip", translator.getZip());
    assertEquals("Country", translator.getCountry());
    assertEquals("Email", translator.getEmail());
    assertEquals("Phone", translator.getPhone());
    assertEquals("Orcid", translator.getOrcid());
  }

  @Override
  protected void populateInitialForm(BaseTranslatorForm<PersonTranslator> translatorForm) {
    selectComboBoxOption("uuid", "UUID");
    selectComboBoxOption("name", "Name");
    selectComboBoxOption("position", "Position");
    selectComboBoxOption("organization", "Organization");
    selectComboBoxOption("street", "Street");
    selectComboBoxOption("city", "City");
    selectComboBoxOption("state", "State");
    selectComboBoxOption("zip", "Zip");
    selectComboBoxOption("country", "Country");
    selectComboBoxOption("phone", "Phone");
    selectComboBoxOption("email", "Email");
    selectComboBoxOption("orcid", "Orcid");
  }
}
