package edu.colorado.cires.pace.cli.command.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.CommandTest;
import edu.colorado.cires.pace.data.object.Person;
import java.util.List;

class PersonCommandTest extends CommandTest<Person> {

  @Override
  public Person createObject(String uniqueField) {
    return Person.builder()
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
}