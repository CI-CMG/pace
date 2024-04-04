package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.DetectionType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class DetectionTypeJsonDatastoreTest extends JsonDatastoreTest<DetectionType, String> {

  @Override
  protected Class<DetectionType> getClazz() {
    return DetectionType.class;
  }

  @Override
  protected JsonDatastore<DetectionType, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<DetectionType> createUUIDProvider() {
    return DetectionType::getUUID;
  }

  @Override
  protected UniqueFieldProvider<DetectionType, String> createUniqueFieldProvider() {
    return DetectionType::getScienceName;
  }

  @Override
  protected DetectionType createNewObject() {
    DetectionType detectionType = new DetectionType();
    detectionType.setUUID(UUID.randomUUID());
    detectionType.setSource(UUID.randomUUID().toString());
    detectionType.setUse(true);
    detectionType.setScienceName(UUID.randomUUID().toString());
    return detectionType;
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getScienceName(), actual.getScienceName());
    assertEquals(expected.getSource(), actual.getSource());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
