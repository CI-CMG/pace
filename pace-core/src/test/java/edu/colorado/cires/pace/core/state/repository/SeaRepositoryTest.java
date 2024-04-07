package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Sea;

class SeaRepositoryTest extends CrudRepositoryTest<Sea, String> {

  @Override
  protected UUIDProvider<Sea> getUUIDPRovider() {
    return Sea::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Sea, String> getUniqueFieldProvider() {
    return Sea::getName;
  }

  @Override
  protected UUIDSetter<Sea> getUUIDSetter() {
    return Sea::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Sea, String> getUniqueFieldSetter() {
    return Sea::setName;
  }

  @Override
  protected CRUDRepository<Sea, String> createRepository() {
    return new SeaRepository(createDatastore());
  }

  @Override
  protected Sea createNewObject(int suffix) {
    Sea sea = new Sea();
    sea.setUse(true);
    sea.setName(String.format("name-%s", suffix));
    return sea;
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
