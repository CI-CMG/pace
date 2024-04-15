package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.function.Function;

class PersonRepositoryTest extends CrudRepositoryTest<Person> {

  @Override
  protected CRUDRepository<Person> createRepository() {
    return new PersonRepository(createDatastore());
  }

  @Override
  protected Function<Person, String> uniqueFieldGetter() {
    return Person::getName;
  }

  @Override
  protected Person createNewObject(int suffix) throws ValidationException {
    return Person.builder()
        .name(String.format("name-%s", suffix))
        .organization(String.format("organization-%s", suffix))
        .position(String.format("position-%s", suffix))
        .street(String.format("street-%s", suffix))
        .city(String.format("city-%s", suffix))
        .state(String.format("state-%s", suffix))
        .zip(String.format("zip-%s", suffix))
        .country(String.format("country-%s", suffix))
        .email(String.format("email-%s", suffix))
        .phone(String.format("phone-%s", suffix))
        .orcid(String.format("orcid-%s", suffix))
        .build();
  }

  @Override
  protected Person copyWithUpdatedUniqueField(Person object, String uniqueField) throws ValidationException {
    return Person.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .organization(object.getOrganization())
        .position(object.getPosition())
        .street(object.getStreet())
        .city(object.getCity())
        .state(object.getState())
        .zip(object.getZip())
        .country(object.getCountry())
        .email(object.getEmail())
        .phone(object.getPhone())
        .orcid(object.getOrcid())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getOrcid(), actual.getOrcid());
    assertEquals(expected.getOrganization(), actual.getOrganization());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getPosition(), actual.getPosition());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getZip(), actual.getZip());
  }
}
