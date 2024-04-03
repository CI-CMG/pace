package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Platform;

class PlatformRepositoryTest extends CrudRepositoryTest<Platform, String> {

  @Override
  protected UUIDProvider<Platform> getUUIDPRovider() {
    return Platform::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Platform, String> getUniqueFieldProvider() {
    return Platform::getName;
  }

  @Override
  protected UUIDSetter<Platform> getUUIDSetter() {
    return Platform::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Platform, String> getUniqueFieldSetter() {
    return Platform::setName;
  }

  @Override
  protected CRUDRepository<Platform, String> createRepository() {
    return new PlatformRepository(createDatastore());
  }

  @Override
  protected Platform createNewObject(int suffix) {
    Platform platform = new Platform();
    platform.setName(String.format("name-%s", suffix));
    platform.setUse(true);
    return platform;
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
