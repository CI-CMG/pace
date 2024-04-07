package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Person;

class PersonRepositoryTest extends CrudRepositoryTest<Person, String> {

  @Override
  protected UUIDProvider<Person> getUUIDPRovider() {
    return Person::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Person, String> getUniqueFieldProvider() {
    return Person::getName;
  }

  @Override
  protected UUIDSetter<Person> getUUIDSetter() {
    return Person::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Person, String> getUniqueFieldSetter() {
    return Person::setName;
  }

  @Override
  protected CRUDRepository<Person, String> createRepository() {
    return new PersonRepository(createDatastore());
  }

  @Override
  protected Person createNewObject(int suffix) {
    Person person = new Person();
    person.setCity(String.format("city-%s", suffix));
    person.setCountry(String.format("country-%s", suffix));
    person.setEmail(String.format("email-%s", suffix));
    person.setName(String.format("name-%s", suffix));
    person.setOrcid(String.format("orcid-%s", suffix));
    person.setOrganization(String.format("organization-%s", suffix));
    person.setPhone(String.format("phone-%s", suffix));
    person.setPosition(String.format("position-%s", suffix));
    person.setState(String.format("state-%s", suffix));
    person.setStreet(String.format("street-%s", suffix));
    person.setUse(true);
    person.setZip(String.format("zip-%s", suffix));
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
