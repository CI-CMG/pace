package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Organization;
import java.io.IOException;
import java.nio.file.Path;
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
  protected Organization createNewObject() {
    return new Organization(
        UUID.randomUUID(),
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
  protected void assertObjectsEqual(Organization expected, Organization actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.city(), actual.city());
    assertEquals(expected.country(), actual.country());
    assertEquals(expected.email(), actual.email());
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.phone(), actual.phone());
    assertEquals(expected.state(), actual.state());
    assertEquals(expected.street(), actual.street());
    assertEquals(expected.zip(), actual.zip());
  }
}
