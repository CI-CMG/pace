package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Platform;

class PlatformRepositoryTest extends CrudRepositoryTest<Platform> {

  @Override
  protected CRUDRepository<Platform> createRepository() {
    return new PlatformRepository(createDatastore());
  }

  @Override
  protected Platform createNewObject(int suffix) {
    return new Platform(
        null,
        String.format("name-%s", suffix)
    );
  }

  @Override
  protected Platform copyWithUpdatedUniqueField(Platform object, String uniqueField) {
    return new Platform(
        object.uuid(),
        uniqueField
    );
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
    assertEquals(expected.name(), actual.name());
  }
}
