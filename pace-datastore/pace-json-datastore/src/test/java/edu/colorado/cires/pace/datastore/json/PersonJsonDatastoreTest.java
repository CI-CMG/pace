package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Person;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class PersonJsonDatastoreTest extends JsonDatastoreTest<Person> {

  @Override
  protected Class<Person> getClazz() {
    return Person.class;
  }

  @Override
  protected JsonDatastore<Person> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new PersonJsonDatastore(storagePath, objectMapper);
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
