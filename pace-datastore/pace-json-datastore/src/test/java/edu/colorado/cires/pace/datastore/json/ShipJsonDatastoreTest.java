package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Ship;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected TypeReference<List<Ship>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Ship createNewObject(int suffix) {
    return Ship.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
  }
}
