package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Ship;

class ShipRepositoryTest extends CrudRepositoryTest<Ship, String> {

  @Override
  protected UUIDProvider<Ship> getUUIDPRovider() {
    return Ship::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Ship, String> getUniqueFieldProvider() {
    return Ship::getName;
  }

  @Override
  protected UUIDSetter<Ship> getUUIDSetter() {
    return Ship::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Ship, String> getUniqueFieldSetter() {
    return Ship::setName;
  }

  @Override
  protected CRUDRepository<Ship, String> createRepository() {
    return new ShipRepository(createDatastore());
  }

  @Override
  protected Ship createNewObject(int suffix) {
    Ship ship = new Ship();
    ship.setUse(true);
    ship.setName(String.format("name-%s", suffix));
    return ship;
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getUUID(), actual.getUUID());
  }
}
