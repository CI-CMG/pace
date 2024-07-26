package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.fileType.FileType;
import edu.colorado.cires.pace.data.object.instrument.Instrument;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

class InstrumentJsonDatastoreTest extends JsonDatastoreTest<Instrument> {

  @Override
  protected Class<Instrument> getClazz() {
    return Instrument.class;
  }

  @Override
  protected JsonDatastore<Instrument> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new InstrumentJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected Instrument createNewObject(int suffix) {
    FileType fileType1 = FileType.builder()
        .uuid(UUID.randomUUID())
        .type(UUID.randomUUID().toString())
        .comment("comment")
        .build();

    FileType fileType2 = FileType.builder()
        .uuid(UUID.randomUUID())
        .type(UUID.randomUUID().toString())
        .build();

    return Instrument.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .fileTypes(List.of(
            fileType1.getType(), fileType2.getType()
        )).build();
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    
    List<String> expectedFileTypes = expected.getFileTypes().stream()
        .sorted()
        .toList();
    
    List<String> actualFileTypes = actual.getFileTypes().stream()
        .sorted()
        .toList();
    
    assertEquals(expectedFileTypes, actualFileTypes);
  }
}
