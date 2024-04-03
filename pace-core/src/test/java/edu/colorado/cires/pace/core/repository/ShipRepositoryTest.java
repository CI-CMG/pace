package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Ship;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    return new ShipRepository() {
      @Override
      public Stream<Ship> findAll() {
        return findAllObjects();
      }

      @Override
      protected Ship save(Ship object) {
        return saveObject(object);
      }

      @Override
      protected void delete(Ship object) {
        deleteObject(object);
      }

      @Override
      protected Optional<Ship> findByUUID(UUID uuid) {
        return findObjectByUUID(uuid);
      }

      @Override
      protected Optional<Ship> findByUniqueField(String uniqueField) {
        return findObjectByUniqueField(uniqueField);
      }
    };
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
