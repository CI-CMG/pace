package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.FileType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class FileTypeJsonDatastoreTest extends JsonDatastoreTest<FileType> {

  @Override
  protected Class<FileType> getClazz() {
    return FileType.class;
  }

  @Override
  protected JsonDatastore<FileType> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "type";
  }

  @Override
  protected FileType createNewObject(int suffix) {
    return FileType.builder()
        .uuid(UUID.randomUUID())
        .type(UUID.randomUUID().toString())
        .comment(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getComment(), actual.getComment());
  }
}
