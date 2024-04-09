package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.DetectionType;
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
  protected DetectionType createNewObject() {
    return new DetectionType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.scienceName(), actual.scienceName());
    assertEquals(expected.source(), actual.source());
  }
}
