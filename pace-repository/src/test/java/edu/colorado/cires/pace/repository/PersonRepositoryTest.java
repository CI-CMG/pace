package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.Person;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.PersonSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PersonRepositoryTest extends CrudRepositoryTest<Person> {

  @Override
  protected CRUDRepository<Person> createRepository() {
    return new PersonRepository(createDatastore());
  }

  @Override
  protected SearchParameters<Person> createSearchParameters(List<Person> objects) {
    return PersonSearchParameters.builder()
        .names(objects.stream().map(Person::getName).toList())
        .build();
  }

  @Override
  protected Person createNewObject(int suffix) {
    return Person.builder()
        .name(String.format("name-%s", suffix))
        .organization(String.format("organization-%s", suffix))
        .position(String.format("position-%s", suffix))
        .street(String.format("street-%s", suffix))
        .city(String.format("city-%s", suffix))
        .state(String.format("state-%s", suffix))
        .zip(String.format("zip-%s", suffix))
        .country(String.format("country-%s", suffix))
        .email(String.format("email-%s", suffix))
        .phone(String.format("phone-%s", suffix))
        .orcid(String.format("orcid-%s", suffix))
        .build();
  }

  @Override
  protected Person copyWithUpdatedUniqueField(Person object, String uniqueField) {
    return Person.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .organization(object.getOrganization())
        .position(object.getPosition())
        .street(object.getStreet())
        .city(object.getCity())
        .state(object.getState())
        .zip(object.getZip())
        .country(object.getCountry())
        .email(object.getEmail())
        .phone(object.getPhone())
        .orcid(object.getOrcid())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Person expected, Person actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
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

  @Test
  void testCreateUUIDNotNull() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Person object = createNewObject(1);
    object = repository.setUUID(object, UUID.randomUUID());

    Person created = repository.create(object);
    assertEquals(object.getUuid(), created.getUuid());
  }

  @Test
  void testCreateUUIDConflict() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Person object1 = repository.create(createNewObject(1));
    Person object2 = object1.toBuilder()
        .name("another-person")
        .build();

    Exception exception = assertThrows(ConflictException.class, () -> repository.create(object2));
    assertEquals(String.format(
        "Person with uuid %s already exists", object1.getUuid()
    ), exception.getMessage());
  }

  @Test
  void testUpdateUUIDsNotEqual() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    Person object = repository.create(createNewObject(1));
    UUID originalUUID = object.getUuid();
    object = object.toBuilder()
        .uuid(UUID.randomUUID())
        .build();

    Person updated = repository.update(originalUUID, object);
    assertEquals(object.getUuid(), updated.getUuid());

    assertThrows(NotFoundException.class, () -> repository.getByUUID(originalUUID));
  }

  @Test
  void testUpdateUUIDConflict() throws Exception {
    Person one = repository.create(createNewObject(1));
    Person two = repository.create(createNewObject(2));

    UUID originalUUID = two.getUuid();
    Person updated = two.toBuilder()
        .uuid(one.getUuid())
        .build();

    Exception exception = assertThrows(ConflictException.class, () -> repository.update(originalUUID, updated));
    assertEquals(String.format(
        "%s with uuid = %s already exists", repository.getClassName(), one.getUuid()
    ), exception.getMessage());
  }
}
