package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class ShipJsonDatastoreTest extends JsonDatastoreTest<Ship, String> {

  @Override
  protected Class<Ship> getClazz() {
    return Ship.class;
  }

  @Override
  protected JsonDatastore<Ship, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new ShipJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Ship> createUUIDProvider() {
    return Ship::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Ship, String> createUniqueFieldProvider() {
    return Ship::getName;
  }

  @Override
  protected Ship createNewObject() {
    Ship ship = new Ship();
    ship.setUUID(UUID.randomUUID());
    ship.setName(UUID.randomUUID().toString());
    ship.setUse(true);
    return ship;
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
