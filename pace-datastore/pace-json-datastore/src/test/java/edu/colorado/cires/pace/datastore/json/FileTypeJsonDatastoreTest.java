package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.FileType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class FileTypeJsonDatastoreTest extends JsonDatastoreTest<FileType> {

  @Override
  protected Class<FileType> getClazz() {
    return FileType.class;
  }

  @Override
  protected JsonDatastore<FileType> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected FileType createNewObject() {
    return new FileType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.type(), actual.type());
    assertEquals(expected.comment(), actual.comment());
  }
}
