package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Ship;

class ShipRepositoryTest extends CrudRepositoryTest<Ship> {

  @Override
  protected CRUDRepository<Ship> createRepository() {
    return new ShipRepository(createDatastore());
  }

  @Override
  protected Ship createNewObject(int suffix) {
    return new Ship(
        null,
        String.format("name-%s", suffix)
    );
  }

  @Override
  protected Ship copyWithUpdatedUniqueField(Ship object, String uniqueField) {
    return new Ship(
        object.uuid(),
        uniqueField
    );
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual, boolean checkUUID) {
    assertEquals(expected.name(), actual.name());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
  }
}
