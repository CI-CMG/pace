package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Organization;
import java.util.List;
import org.assertj.swing.fixture.JPanelFixture;

class OrganizationsPanelTest extends MetadataPanelTest<Organization> {

  @Override
  protected String getJsonFileName() {
    return "organizations.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Organizations";
  }

  @Override
  protected String getMetadataPanelName() {
    return "organizationsPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "organizationForm";
  }

  @Override
  protected void fillOutForm(JPanelFixture formFixture, Organization object) {
    enterFieldText(formFixture, "name", object.getName());
    enterFieldText(formFixture, "street", object.getStreet());
    enterFieldText(formFixture, "city", object.getCity());
    enterFieldText(formFixture, "state", object.getState());
    enterFieldText(formFixture, "zip", object.getZip());
    enterFieldText(formFixture, "country", object.getCountry());
    enterFieldText(formFixture, "email", object.getEmail());
    enterFieldText(formFixture, "phone", object.getPhone());
  }

  @Override
  protected Organization createObject(String uniqueField) {
    return Organization.builder()
        .name(uniqueField)
        .street("a")
        .city("b")
        .state("c")
        .zip("d")
        .country("e")
        .email("f")
        .phone("g")
        .build();
  }

  @Override
  protected String[] objectToRow(Organization object) {
    return new String[] {
        object.getName(), object.getStreet(), object.getCity(), object.getState(),
        object.getZip(), object.getCountry(), object.getEmail(), object.getPhone()
    };
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, Organization object) {
    requireFieldText(formFixture, "uuid", object.getUuid().toString());
    requireFieldText(formFixture, "name", object.getName());
    requireFieldText(formFixture, "street", object.getStreet());
    requireFieldText(formFixture, "city", object.getCity());
    requireFieldText(formFixture, "state", object.getState());
    requireFieldText(formFixture, "zip", object.getZip());
    requireFieldText(formFixture, "country", object.getCountry());
    requireFieldText(formFixture, "email", object.getEmail());
    requireFieldText(formFixture, "phone", object.getPhone());
  }

  @Override
  protected Organization updateObject(Organization original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .street("z")
        .city("x")
        .build();
  }

  @Override
  protected String getUniqueField(Organization object) {
    return object.getName();
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getPhone(), actual.getPhone());
  }

  @Override
  protected TypeReference<List<Organization>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Organization> getObjectClass() {
    return Organization.class;
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
        "UUID", "Name", "Street", "City", "State", "Zip", "Country", "Email", "Phone"
    };
  }

  @Override
  protected void fillOutTranslatorForm(JPanelFixture panelFixture) {
    selectComboBoxOption(panelFixture, "translatorType", "Organization", 11);
    
    JPanelFixture formFixture = getPanel(panelFixture, "organizationTranslatorForm");
    selectComboBoxOption(formFixture, "uuid", "UUID", 9);
    selectComboBoxOption(formFixture, "name", "Name", 9);
    selectComboBoxOption(formFixture, "street", "Street", 9);
    selectComboBoxOption(formFixture, "city", "City", 9);
    selectComboBoxOption(formFixture, "state", "State", 9);
    selectComboBoxOption(formFixture, "zip", "Zip", 9);
    selectComboBoxOption(formFixture, "country", "Country", 9);
    selectComboBoxOption(formFixture, "phone", "Phone", 9);
    selectComboBoxOption(formFixture, "email", "Email", 9);
  }
}
