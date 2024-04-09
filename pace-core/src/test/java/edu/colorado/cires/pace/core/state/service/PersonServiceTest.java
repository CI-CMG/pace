package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.PersonRepository;
import edu.colorado.cires.pace.data.Person;
import java.util.UUID;

class PersonServiceTest extends CrudServiceTest<Person, PersonRepository> {

  @Override
  protected Class<PersonRepository> getRepositoryClass() {
    return PersonRepository.class;
  }

  @Override
  protected CRUDService<Person> createService(PersonRepository repository) {
    return new PersonService(repository);
  }

  @Override
  protected Person createNewObject() {
    return new Person(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual) {
    assertEquals(expected.uuid(), actual.uuid());
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
