package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
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
  protected TypeReference<List<Instrument>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Instrument createNewObject() {
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
            fileType1, fileType2
        )).build();
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
    
    List<FileType> expectedFileTypes = expected.getFileTypes().stream()
        .sorted(Comparator.comparing(FileType::getUuid))
        .toList();
    
    List<FileType> actualFileTypes = actual.getFileTypes().stream()
        .sorted(Comparator.comparing(FileType::getUuid))
        .toList();
    
    assertEquals(expectedFileTypes.size(), actualFileTypes.size());
    
    for (int i = 0; i < expectedFileTypes.size(); i++) {
      assertEquals(expectedFileTypes.get(i).getUuid(), actualFileTypes.get(i).getUuid());
      assertEquals(expectedFileTypes.get(i).getType(), actualFileTypes.get(i).getType());
      assertEquals(expectedFileTypes.get(i).getComment(), actualFileTypes.get(i).getComment());
    }
  }
}
