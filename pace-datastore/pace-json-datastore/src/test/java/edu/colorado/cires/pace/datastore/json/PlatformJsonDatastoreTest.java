package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Platform;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class PlatformJsonDatastoreTest extends JsonDatastoreTest<Platform> {

  @Override
  protected Class<Platform> getClazz() {
    return Platform.class;
  }

  @Override
  protected JsonDatastore<Platform> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new PlatformJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected Platform createNewObject() {
    return new Platform(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.name(), actual.name());
  }
}
