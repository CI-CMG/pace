package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.AudioPackage;
import edu.colorado.cires.pace.data.object.DetectionsPackage;
import edu.colorado.cires.pace.data.object.Organization;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.OrganizationSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationRepositoryTest extends PackageDependencyRepositoryTest<Organization> {

  @Override
  protected CRUDRepository<Organization> createRepository() {
    return new OrganizationRepository(createDatastore(), createDatastore(packages, Package.class, "packageId"));
  }

  @Override
  protected SearchParameters<Organization> createSearchParameters(List<Organization> objects) {
    return OrganizationSearchParameters.builder()
        .names(objects.stream().map(Organization::getName).toList())
        .build();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected Class<Organization> getObjectClass() {
    return Organization.class;
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
    object = (Organization) object.setUuid(UUID.randomUUID());

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

  @Override
  protected boolean objectInDependentObject(Organization updated, UUID dependentObjectUUID) {
    String name = updated.getName();
    Package p = packages.get(dependentObjectUUID);
    return p.getSponsors().contains(name) || p.getFunders().contains(name);
  }

  @Override
  protected Package createAndSaveDependentObject(Organization object) {
    Package p = ((DetectionsPackage) PackageRepositoryTest.createDetectionsDataset(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .sponsors(Collections.singletonList(object.getName()))
        .funders(Collections.singletonList(object.getName()))
        .build();
    
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }

  @Override
  protected Package createAndSaveIndependentDependentObject() {
    Package p = ((AudioPackage) PackageRepositoryTest.createAudioPackingJob(1)).toBuilder()
        .uuid(UUID.randomUUID())
        .sponsors(Collections.singletonList("unrelated-sponsor"))
        .funders(Collections.singletonList("unrelated-funder"))
        .build();
    packages.put(p.getUuid(), p);
    return packages.get(p.getUuid());
  }
}
