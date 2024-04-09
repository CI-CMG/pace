package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
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
  protected Instrument createNewObject() {
    FileType fileType1 = new FileType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        "comment"
    );

    FileType fileType2 = new FileType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        "comment"
    );

    return new Instrument(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        List.of(
            fileType1, fileType2
        )
    );
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.name(), actual.name());
    
    List<FileType> expectedFileTypes = expected.fileTypes().stream()
        .sorted(Comparator.comparing(FileType::uuid))
        .toList();
    
    List<FileType> actualFileTypes = actual.fileTypes().stream()
        .sorted(Comparator.comparing(FileType::uuid))
        .toList();
    
    assertEquals(expectedFileTypes.size(), actualFileTypes.size());
    
    for (int i = 0; i < expectedFileTypes.size(); i++) {
      assertEquals(expectedFileTypes.get(i).uuid(), actualFileTypes.get(i).uuid());
      assertEquals(expectedFileTypes.get(i).type(), actualFileTypes.get(i).type());
      assertEquals(expectedFileTypes.get(i).comment(), actualFileTypes.get(i).comment());
    }
  }
}
