package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Sea;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class SeaJsonDatastoreTest extends JsonDatastoreTest<Sea, String> {

  @Override
  protected Class<Sea> getClazz() {
    return Sea.class;
  }

  @Override
  protected JsonDatastore<Sea, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new SeaJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Sea> createUUIDProvider() {
    return Sea::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Sea, String> createUniqueFieldProvider() {
    return Sea::getName;
  }

  @Override
  protected Sea createNewObject() {
    Sea sea = new Sea();
    sea.setUUID(UUID.randomUUID());
    sea.setName(UUID.randomUUID().toString());
    sea.setUse(true);
    return sea;
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
