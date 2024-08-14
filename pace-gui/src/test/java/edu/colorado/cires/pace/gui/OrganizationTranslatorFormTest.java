package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;

import edu.colorado.cires.pace.data.object.contact.organization.translator.OrganizationTranslator;

public class OrganizationTranslatorFormTest extends BaseTranslatorFormTest<OrganizationTranslator, OrganizationTranslatorForm> {
  
  @Override
  protected String[] getHeaderOptions() {
    return new String[] {
        "UUID", "Name", "Street", "City", "State", "Zip", "Country", "Phone", "Email"
    };
  }

  @Override
  protected OrganizationTranslatorForm createForm(OrganizationTranslator organizationTranslator, String[] headerOptions) {
    return new OrganizationTranslatorForm(organizationTranslator, headerOptions);
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(OrganizationTranslator translator) {
    assertEquals("UUID", translator.getOrganizationUUID());
    assertEquals("Name", translator.getOrganizationName());
    assertEquals("Street", translator.getStreet());
    assertEquals("City", translator.getCity());
    assertEquals("State", translator.getState());
    assertEquals("Zip", translator.getZip());
    assertEquals("Country", translator.getCountry());
    assertEquals("Phone", translator.getPhone());
    assertEquals("Email", translator.getEmail());
    
  }

  @Override
  protected void populateInitialForm(BaseTranslatorForm<OrganizationTranslator> translatorForm) {
    selectComboBoxOption("uuid", "UUID");
    selectComboBoxOption("name", "Name");
    selectComboBoxOption("street", "Street");
    selectComboBoxOption("city", "City");
    selectComboBoxOption("state", "State");
    selectComboBoxOption("zip", "Zip");
    selectComboBoxOption("country", "Country");
    selectComboBoxOption("phone", "Phone");
    selectComboBoxOption("email", "Email");
  }
}
