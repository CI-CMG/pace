package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Sea;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class SeaJsonDatastoreTest extends JsonDatastoreTest<Sea> {

  @Override
  protected Class<Sea> getClazz() {
    return Sea.class;
  }

  @Override
  protected JsonDatastore<Sea> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new SeaJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected Sea createNewObject() {
    return new Sea(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.name(), actual.name());
  }
}
