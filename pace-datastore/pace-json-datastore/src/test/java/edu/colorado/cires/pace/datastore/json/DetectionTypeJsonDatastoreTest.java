package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.DetectionType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class DetectionTypeJsonDatastoreTest extends JsonDatastoreTest<DetectionType> {

  @Override
  protected Class<DetectionType> getClazz() {
    return DetectionType.class;
  }

  @Override
  protected JsonDatastore<DetectionType> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "source";
  }

  @Override
  protected DetectionType createNewObject(int suffix) {
    return DetectionType.builder()
        .uuid(UUID.randomUUID())
        .source(UUID.randomUUID().toString())
        .scienceName(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getScienceName(), actual.getScienceName());
    assertEquals(expected.getSource(), actual.getSource());
  }
}
