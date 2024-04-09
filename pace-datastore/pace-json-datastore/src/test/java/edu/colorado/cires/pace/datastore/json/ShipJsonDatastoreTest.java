package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Ship;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class ShipJsonDatastoreTest extends JsonDatastoreTest<Ship> {

  @Override
  protected Class<Ship> getClazz() {
    return Ship.class;
  }

  @Override
  protected JsonDatastore<Ship> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new ShipJsonDatastore(storagePath, objectMapper);
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
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected. name(), actual. name());
  }
}
