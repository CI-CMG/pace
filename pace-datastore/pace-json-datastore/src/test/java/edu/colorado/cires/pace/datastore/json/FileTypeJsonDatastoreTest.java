package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.FileType;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class FileTypeJsonDatastoreTest extends JsonDatastoreTest<FileType, String> {

  @Override
  protected Class<FileType> getClazz() {
    return FileType.class;
  }

  @Override
  protected JsonDatastore<FileType, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<FileType> createUUIDProvider() {
    return FileType::getUUID;
  }

  @Override
  protected UniqueFieldProvider<FileType, String> createUniqueFieldProvider() {
    return FileType::getType;
  }

  @Override
  protected FileType createNewObject() {
    FileType fileType = new FileType();
    fileType.setUUID(UUID.randomUUID());
    fileType.setType(UUID.randomUUID().toString());
    fileType.setComment(UUID.randomUUID().toString());
    fileType.setUse(true);
    return fileType;
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getComment(), actual.getComment());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
