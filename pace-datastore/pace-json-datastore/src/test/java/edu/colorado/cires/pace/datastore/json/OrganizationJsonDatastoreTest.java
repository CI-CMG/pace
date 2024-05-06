package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Organization;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class OrganizationJsonDatastoreTest extends JsonDatastoreTest<Organization> {

  @Override
  protected Class<Organization> getClazz() {
    return Organization.class;
  }

  @Override
  protected JsonDatastore<Organization> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new OrganizationJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected TypeReference<List<Organization>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Organization createNewObject() {
    return Organization.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .street(UUID.randomUUID().toString())
        .city(UUID.randomUUID().toString())
        .state(UUID.randomUUID().toString())
        .zip(UUID.randomUUID().toString())
        .country(UUID.randomUUID().toString())
        .email(UUID.randomUUID().toString())
        .phone(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getZip(), actual.getZip());
  }
}
