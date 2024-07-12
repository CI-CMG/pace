package edu.colorado.cires.pace.cli.command.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.ObjectWithEditableUUIDCommandTest;
import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.translator.PersonTranslator;
import java.util.List;
import java.util.UUID;

public class PersonCommandTest extends ObjectWithEditableUUIDCommandTest<Person, PersonTranslator> {

  @Override
  public Person createObject(String uniqueField, boolean withUUID) {
    return Person.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .organization("org")
        .position("position")
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .orcid("orcid")
        .build();
  }

  @Override
  protected String getRepositoryFileName() {
    return "people.json";
  }

  @Override
  protected String getCommandPrefix() {
    return "person";
  }

  @Override
  protected TypeReference<List<Person>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Person> getClazz() {
    return Person.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual, boolean checkUUID) {
    assertPeopleEqual(expected, actual, checkUUID);
  }
  
  public static void assertPeopleEqual(Person expected, Person actual, boolean checkUUID) {
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
  protected String getUniqueField(Person object) {
    return object.getName();
  }

  @Override
  protected Person updateObject(Person original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "personName",
        "organization",
        "position",
        "street",
        "city",
        "state",
        "zip",
        "country",
        "email",
        "phone",
        "orcid"
    };
  }

  @Override
  protected PersonTranslator createTranslator(String name) {
    return PersonTranslator.builder()
        .name(name)
        .personUUID("UUID")
        .personName("personName")
        .organization("organization")
        .position("position")
        .street("street")
        .city("city")
        .state("state")
        .zip("zip")
        .country("country")
        .email("email")
        .phone("phone")
        .orcid("orcid")
        .build();
  }

  @Override
  protected String[] objectToRow(Person object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
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
}