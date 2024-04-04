package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Person;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class PersonJsonDatastoreTest extends JsonDatastoreTest<Person, String> {

  @Override
  protected Class<Person> getClazz() {
    return Person.class;
  }

  @Override
  protected JsonDatastore<Person, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new PersonJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Person> createUUIDProvider() {
    return Person::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Person, String> createUniqueFieldProvider() {
    return Person::getName;
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
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getZip(), actual.getZip());
  }
}
