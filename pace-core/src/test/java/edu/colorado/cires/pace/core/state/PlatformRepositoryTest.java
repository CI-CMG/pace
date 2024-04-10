package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Platform;

class PlatformRepositoryTest extends CrudRepositoryTest<Platform> {

  @Override
  protected CRUDRepository<Platform> createRepository() {
    return new PlatformRepository(createDatastore());
  }

  @Override
  protected Platform createNewObject(int suffix) {
    return Platform.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Platform copyWithUpdatedUniqueField(Platform object, String uniqueField) {
    return Platform.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
  }
}
