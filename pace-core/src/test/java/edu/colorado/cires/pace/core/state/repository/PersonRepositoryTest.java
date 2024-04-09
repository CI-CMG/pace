package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Person;

class PersonRepositoryTest extends CrudRepositoryTest<Person> {

  @Override
  protected CRUDRepository<Person> createRepository() {
    return new PersonRepository(createDatastore());
  }

  @Override
  protected Person createNewObject(int suffix) {
    return new Person(
        null,
        String.format("name-%s", suffix),
        String.format("organization-%s", suffix),
        String.format("position-%s", suffix),
        String.format("street-%s", suffix),
        String.format("city-%s", suffix),
        String.format("state-%s", suffix),
        String.format("zip-%s", suffix),
        String.format("country-%s", suffix),
        String.format("email-%s", suffix),
        String.format("phone-%s", suffix),
        String.format("orcid-%s", suffix)
    );
  }

  @Override
  protected Person copyWithUpdatedUniqueField(Person object, String uniqueField) {
    return new Person(
        object.uuid(),
        uniqueField,
        object.organization(),
        object.position(),
        object.street(),
        object.city(),
        object.state(),
        object.zip(),
        object.country(),
        object.email(),
        object.phone(),
        object.orcid()
    );
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
    assertEquals(expected.city(), actual.city());
    assertEquals(expected.country(), actual.country());
    assertEquals(expected.email(), actual.email());
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.orcid(), actual.orcid());
    assertEquals(expected.organization(), actual.organization());
    assertEquals(expected.phone(), actual.phone());
    assertEquals(expected.position(), actual.position());
    assertEquals(expected.state(), actual.state());
    assertEquals(expected.street(), actual.street());
    assertEquals(expected.zip(), actual.zip());
  }
}
