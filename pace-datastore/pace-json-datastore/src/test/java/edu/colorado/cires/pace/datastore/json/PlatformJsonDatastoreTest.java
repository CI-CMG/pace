package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.validation.ValidationException;
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
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected Platform createNewObject() throws ValidationException {
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
