package edu.colorado.cires.pace.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Person;
import java.util.List;
import org.assertj.swing.fixture.JPanelFixture;

class PeoplePanelTest extends MetadataPanelTest<Person> {

  @Override
  protected String getJsonFileName() {
    return "people.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "People";
  }

  @Override
  protected String getMetadataPanelName() {
    return "peoplePanel";
  }

  @Override
  protected String getFormPanelName() {
    return "personForm";
  }

  @Override
  protected void fillOutForm(JPanelFixture formFixture, Person object) {
    getTextFixture(formFixture, "uuid").requireEnabled();
    enterFieldText(formFixture, "name", object.getName());
    enterFieldText(formFixture, "organization", object.getOrganization());
    enterFieldText(formFixture, "position", object.getPosition());
    enterFieldText(formFixture, "street", object.getStreet());
    enterFieldText(formFixture, "city", object.getCity());
    enterFieldText(formFixture, "state", object.getState());
    enterFieldText(formFixture, "zip", object.getZip());
    enterFieldText(formFixture, "country", object.getCountry());
    enterFieldText(formFixture, "phone", object.getPhone());
    enterFieldText(formFixture, "email", object.getEmail());
    enterFieldText(formFixture, "orcid", object.getOrcid());
  }

  @Override
  protected Person createObject(String uniqueField) {
    return Person.builder()
        .name(uniqueField)
        .position("b")
        .organization("c")
        .street("d")
        .city("e")
        .state("f")
        .zip("g")
        .country("h")
        .phone("i")
        .email("j")
        .orcid("k")
        .build();
  }

  @Override
  protected String[] objectToRow(Person object) {
    return new String[] {
        object.getName(),
        object.getOrganization(),
        object.getPosition(),
        object.getStreet(),
        object.getCity(),
        object.getState(),
        object.getZip(),
        object.getCountry(),
        object.getEmail(),
        object.getPhone(),
        object.getOrcid()
    };
  }

  @Override
  protected void requireFormContents(JPanelFixture formFixture, Person object) {
    requireFieldText(formFixture, "uuid", object.getUuid().toString());
    requireFieldText(formFixture, "name", object.getName());
    requireFieldText(formFixture, "position", object.getPosition());
    requireFieldText(formFixture, "organization", object.getOrganization());
    requireFieldText(formFixture, "street", object.getStreet());
    requireFieldText(formFixture, "city", object.getCity());
    requireFieldText(formFixture, "state", object.getState());
    requireFieldText(formFixture, "zip", object.getZip());
    requireFieldText(formFixture, "country", object.getCountry());
    requireFieldText(formFixture, "phone", object.getPhone());
    requireFieldText(formFixture, "email", object.getEmail());
    requireFieldText(formFixture, "orcid", object.getOrcid());
  }

  @Override
  protected Person updateObject(Person original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .street("street")
        .city("city")
        .build();
  }

  @Override
  protected String getUniqueField(Person object) {
    return object.getName();
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }

    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getOrganization(), actual.getOrganization());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getZip(), actual.getZip());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getOrcid(), actual.getOrcid());
  }

  @Override
  protected TypeReference<List<Person>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Person> getObjectClass() {
    return Person.class;
  }

  @Override
  protected String[] getSpreadsheetHeaders() {
    return new String[] {
        "UUID", "Name", "Organization", "Position", "Street", "City", 
        "State", "Zip", "Country", "Email", "Phone", "Orcid"
    };
  }

  @Override
  protected void fillOutTranslatorForm(JPanelFixture panelFixture) {
    selectComboBoxOption(panelFixture, "translatorType", "Person", 11);
    JPanelFixture formFixture = getPanel(panelFixture, "personTranslatorForm");
    selectComboBoxOption(formFixture, "uuid", "UUID", 12);
    selectComboBoxOption(formFixture, "name", "Name", 12);
    selectComboBoxOption(formFixture, "position", "Position", 12);
    selectComboBoxOption(formFixture, "organization", "Organization", 12);
    selectComboBoxOption(formFixture, "street", "Street", 12);
    selectComboBoxOption(formFixture, "city", "City", 12);
    selectComboBoxOption(formFixture, "state", "State", 12);
    selectComboBoxOption(formFixture, "zip", "Zip", 12);
    selectComboBoxOption(formFixture, "country", "Country", 12);
    selectComboBoxOption(formFixture, "email", "Email", 12);
    selectComboBoxOption(formFixture, "phone", "Phone", 12);
    selectComboBoxOption(formFixture, "orcid", "Orcid", 12);
  }


}
