package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.OrganizationSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class OrganizationRepositoryTest extends CrudRepositoryTest<Organization> {

  @Override
  protected CRUDRepository<Organization> createRepository() {
    return new OrganizationRepository(createDatastore());
  }

  @Override
  protected Function<Organization, String> uniqueFieldGetter() {
    return Organization::getName;
  }

  @Override
  protected SearchParameters<Organization> createSearchParameters(List<Organization> objects) {
    return OrganizationSearchParameters.builder()
        .names(objects.stream().map(Organization::getName).toList())
        .build();
  }

  @Override
  protected Organization createNewObject(int suffix) {
    return Organization.builder()
        .name(String.format("name-%s", suffix))
        .street(String.format("street-%s", suffix))
        .city(String.format("city-%s", suffix))
        .state(String.format("state-%s", suffix))
        .zip(String.format("zip-%s", suffix))
        .country(String.format("country-%s", suffix))
        .email(String.format("email-%s", suffix))
        .phone(String.format("phone-%s", suffix))
        .build();
  }

  @Override
  protected Organization copyWithUpdatedUniqueField(Organization object, String uniqueField) {
    return Organization.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .street(object.getStreet())
        .city(object.getCity())
        .state(object.getState())
        .zip(object.getZip())
        .country(object.getCountry())
        .email(object.getEmail())
        .phone(object.getPhone())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getCountry(), actual.getCountry());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPhone(), actual.getPhone());
    assertEquals(expected.getState(), actual.getState());
    assertEquals(expected.getStreet(), actual.getStreet());
    assertEquals(expected.getZip(), actual.getZip());
  }

  @Test
  void testCreateUUIDNotNull() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Organization object = createNewObject(1);
    object = repository.setUUID(object, UUID.randomUUID());

    Organization created = repository.create(object);
    assertEquals(object.getUuid(), created.getUuid());
  }
  
  @Test
  void testCreateUUIDConflict() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Organization object1 = repository.create(createNewObject(1));
    Organization object2 = object1.toBuilder()
        .name("another-organization")
        .build();
    
    Exception exception = assertThrows(ConflictException.class, () -> repository.create(object2));
    assertEquals(String.format(
        "Organization with uuid %s already exists", object1.getUuid()
    ), exception.getMessage());
  }

  @Test
  void testUpdateUUIDsNotEqual() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Organization object = repository.create(createNewObject(1));
    UUID originalUUID = object.getUuid();
    object = object.toBuilder()
        .uuid(UUID.randomUUID())
        .build();
    
    Organization updated = repository.update(originalUUID, object);
    assertEquals(object.getUuid(), updated.getUuid());
    
    assertThrows(NotFoundException.class, () -> repository.getByUUID(originalUUID));
  }

  @Test
  void testUpdateUUIDConflict() throws Exception {
    Organization one = repository.create(createNewObject(1));
    Organization two = repository.create(createNewObject(2));

    UUID originalUUID = two.getUuid();
    Organization updated = two.toBuilder()
        .uuid(one.getUuid())
        .build();

    Exception exception = assertThrows(ConflictException.class, () -> repository.update(originalUUID, updated));
    assertEquals(String.format(
        "%s with uuid = %s already exists", repository.getClassName(), one.getUuid()
    ), exception.getMessage());
  }
}
