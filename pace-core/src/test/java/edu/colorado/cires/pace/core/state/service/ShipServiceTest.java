package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.ShipRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Ship;
import java.util.UUID;

class ShipServiceTest extends CrudServiceTest<Ship, String, ShipRepository> {

  @Override
  protected Class<ShipRepository> getRepositoryClass() {
    return ShipRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Ship, String> getUniqueFieldProvider() {
    return Ship::getName;
  }

  @Override
  protected UUIDProvider<Ship> getUUIDProvider() {
    return Ship::getUUID;
  }

  @Override
  protected CRUDService<Ship, String> createService(ShipRepository repository) {
    return new ShipService(repository);
  }

  @Override
  protected Ship createNewObject() {
    Ship ship = new Ship();
    ship.setName(UUID.randomUUID().toString());
    ship.setUse(true);
    ship.setUUID(UUID.randomUUID());
    return ship;
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getName(), actual.getName());
  }
}
