package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.AudioSensor;
import edu.colorado.cires.pace.data.object.Sensor;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class SensorJsonDatastoreTest extends JsonDatastoreTest<Sensor> {

  @Override
  protected Class<Sensor> getClazz() {
    return Sensor.class;
  }

  @Override
  protected JsonDatastore<Sensor> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new SensorJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected Sensor createNewObject(int suffix) {
    return AudioSensor.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .description(UUID.randomUUID().toString())
        .hydrophoneId(UUID.randomUUID().toString())
        .preampId(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Sensor expected, Sensor actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getDescription(), actual.getDescription());
  }
}
