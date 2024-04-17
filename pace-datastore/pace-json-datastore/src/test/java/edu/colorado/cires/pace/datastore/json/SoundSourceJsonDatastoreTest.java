package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.SoundSource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class SoundSourceJsonDatastoreTest extends JsonDatastoreTest<SoundSource> {

  @Override
  protected Class<SoundSource> getClazz() {
    return SoundSource.class;
  }

  @Override
  protected JsonDatastore<SoundSource> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new SoundSourceJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected SoundSource createNewObject() {
    return SoundSource.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .scientificName(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(SoundSource expected, SoundSource actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getScientificName(), actual.getScientificName());
  }
}
