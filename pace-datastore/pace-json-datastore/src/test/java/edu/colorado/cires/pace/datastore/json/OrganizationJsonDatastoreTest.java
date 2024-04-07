package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Organization;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class OrganizationJsonDatastoreTest extends JsonDatastoreTest<Organization, String> {

  @Override
  protected Class<Organization> getClazz() {
    return Organization.class;
  }

  @Override
  protected JsonDatastore<Organization, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new OrganizationJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Organization> createUUIDProvider() {
    return Organization::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Organization, String> createUniqueFieldProvider() {
    return Organization::getName;
  }

  @Override
  protected Organization createNewObject() {
    Organization organization = new Organization();
    organization.setCity(UUID.randomUUID().toString());
    organization.setCountry(UUID.randomUUID().toString());
    organization.setEmail(UUID.randomUUID().toString());
    organization.setName(UUID.randomUUID().toString());
    organization.setPhone(UUID.randomUUID().toString());
    organization.setState(UUID.randomUUID().toString());
    organization.setStreet(UUID.randomUUID().toString());
    organization.setUse(true);
    organization.setUUID(UUID.randomUUID());
    organization.setZip(UUID.randomUUID().toString());
    return organization;
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual) {
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getZip(), actual.getZip());
  }
}
