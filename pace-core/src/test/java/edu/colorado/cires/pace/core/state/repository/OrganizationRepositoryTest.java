package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Organization;

class OrganizationRepositoryTest extends CrudRepositoryTest<Organization> {

  @Override
  protected CRUDRepository<Organization> createRepository() {
    return new OrganizationRepository(createDatastore());
  }

  @Override
  protected Organization createNewObject(int suffix) {
    return new Organization(
        null,
        String.format("name-%s", suffix),
        String.format("street-%s", suffix),
        String.format("city-%s", suffix),
        String.format("state-%s", suffix),
        String.format("zip-%s", suffix),
        String.format("country-%s", suffix),
        String.format("email-%s", suffix),
        String.format("phone-%s", suffix)
    );
  }

  @Override
  protected Organization copyWithUpdatedUniqueField(Organization object, String uniqueField) {
    return new Organization(
        object.uuid(),
        uniqueField,
        object.street(),
        object.city(),
        object.state(),
        object.zip(),
        object.country(),
        object.email(),
        object.phone()
    );
  }

  @Override
  protected void assertObjectsEqual(Organization expected, Organization actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
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
