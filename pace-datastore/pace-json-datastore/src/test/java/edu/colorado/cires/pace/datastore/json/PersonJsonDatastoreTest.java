package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Person;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected TypeReference<List<Person>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Person createNewObject(int suffix) {
    return Person.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .organization(UUID.randomUUID().toString())
        .position(UUID.randomUUID().toString())
        .street(UUID.randomUUID().toString())
        .city(UUID.randomUUID().toString())
        .state(UUID.randomUUID().toString())
        .zip(UUID.randomUUID().toString())
        .country(UUID.randomUUID().toString())
        .email(UUID.randomUUID().toString())
        .phone(UUID.randomUUID().toString())
        .orcid(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
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
