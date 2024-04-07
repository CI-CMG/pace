package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.PersonRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Person;
import java.util.UUID;

class PersonServiceTest extends CrudServiceTest<Person, String, PersonRepository> {

  @Override
  protected Class<PersonRepository> getRepositoryClass() {
    return PersonRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Person, String> getUniqueFieldProvider() {
    return Person::getName;
  }

  @Override
  protected UUIDProvider<Person> getUUIDProvider() {
    return Person::getUUID;
  }

  @Override
  protected CRUDService<Person, String> createService(PersonRepository repository) {
    return new PersonService(repository);
  }

  @Override
  protected Person createNewObject() {
    Person person = new Person();
    person.setCity(UUID.randomUUID().toString());
    person.setCountry(UUID.randomUUID().toString());
    person.setEmail(UUID.randomUUID().toString());
    person.setName(UUID.randomUUID().toString());
    person.setOrcid(UUID.randomUUID().toString());
    person.setOrganization(UUID.randomUUID().toString());
    person.setPhone(UUID.randomUUID().toString());
    person.setPosition(UUID.randomUUID().toString());
    person.setState(UUID.randomUUID().toString());
    person.setStreet(UUID.randomUUID().toString());
    person.setUse(true);
    person.setUUID(UUID.randomUUID());
    person.setZip(UUID.randomUUID().toString());
    return person;
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual) {
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
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getZip(), actual.getZip());
  }
}
