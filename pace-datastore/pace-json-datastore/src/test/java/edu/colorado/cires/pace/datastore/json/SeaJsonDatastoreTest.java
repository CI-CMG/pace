package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.sea.Sea;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected Sea createNewObject(int suffix) {
    return Sea.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
  }
}
