package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Sea;

class SeaRepositoryTest extends CrudRepositoryTest<Sea> {

  @Override
  protected CRUDRepository<Sea> createRepository() {
    return new SeaRepository(createDatastore());
  }

  @Override
  protected Sea createNewObject(int suffix) {
    return new Sea(
        null,
        String.format("name-%s", suffix)
    );
  }

  @Override
  protected Sea copyWithUpdatedUniqueField(Sea object, String uniqueField) {
    return new Sea(
        object.uuid(),
        uniqueField
    );
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual, boolean checkUUID) {
    assertEquals(expected.name(), actual.name());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
  }
}
