package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.ShipRepository;
import edu.colorado.cires.pace.data.Ship;
import java.util.UUID;

class ShipServiceTest extends CrudServiceTest<Ship, ShipRepository> {

  @Override
  protected Class<ShipRepository> getRepositoryClass() {
    return ShipRepository.class;
  }

  @Override
  protected CRUDService<Ship> createService(ShipRepository repository) {
    return new ShipService(repository);
  }

  @Override
  protected Ship createNewObject() {
    return new Ship(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual) {
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.uuid(), actual.uuid());
  }
}
