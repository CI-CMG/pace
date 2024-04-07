package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Platform;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class PlatformJsonDatastoreTest extends JsonDatastoreTest<Platform, String> {

  @Override
  protected Class<Platform> getClazz() {
    return Platform.class;
  }

  @Override
  protected JsonDatastore<Platform, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new PlatformJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Platform> createUUIDProvider() {
    return Platform::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Platform, String> createUniqueFieldProvider() {
    return Platform::getName;
  }

  @Override
  protected Platform createNewObject() {
    Platform platform = new Platform();
    platform.setUUID(UUID.randomUUID());
    platform.setName(UUID.randomUUID().toString());
    platform.setUse(true);
    return platform;
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
