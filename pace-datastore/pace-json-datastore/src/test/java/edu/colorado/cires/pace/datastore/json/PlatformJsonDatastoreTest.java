package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Platform;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected TypeReference<List<Platform>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Platform createNewObject(int suffix) {
    return Platform.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Platform expected, Platform actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
  }
}
