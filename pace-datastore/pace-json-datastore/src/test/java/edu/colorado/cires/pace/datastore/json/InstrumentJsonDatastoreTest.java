package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

class InstrumentJsonDatastoreTest extends JsonDatastoreTest<Instrument, String> {

  @Override
  protected Class<Instrument> getClazz() {
    return Instrument.class;
  }

  @Override
  protected JsonDatastore<Instrument, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new InstrumentJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Instrument> createUUIDProvider() {
    return Instrument::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Instrument, String> createUniqueFieldProvider() {
    return Instrument::getName;
  }

  @Override
  protected Instrument createNewObject() {
    Instrument instrument = new Instrument();
    instrument.setUUID(UUID.randomUUID());
    instrument.setName(UUID.randomUUID().toString());
    instrument.setUse(true);

    FileType fileType1 = new FileType();
    fileType1.setUUID(UUID.randomUUID());
    fileType1.setComment(UUID.randomUUID().toString());
    fileType1.setUse(true);
    fileType1.setType(UUID.randomUUID().toString());

    FileType fileType2 = new FileType();
    fileType2.setUUID(UUID.randomUUID());
    fileType2.setComment(UUID.randomUUID().toString());
    fileType2.setUse(true);
    fileType2.setType(UUID.randomUUID().toString());

    instrument.setFileTypes(List.of(
        fileType1, fileType2
    ));

    return instrument;
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
    
    List<FileType> expectedFileTypes = expected.getFileTypes().stream()
        .sorted(Comparator.comparing(FileType::getUUID))
        .toList();
    
    List<FileType> actualFileTypes = actual.getFileTypes().stream()
        .sorted(Comparator.comparing(FileType::getUUID))
        .toList();
    
    assertEquals(expectedFileTypes.size(), actualFileTypes.size());
    
    for (int i = 0; i < expectedFileTypes.size(); i++) {
      assertEquals(expectedFileTypes.get(i).getUUID(), actualFileTypes.get(i).getUUID());
      assertEquals(expectedFileTypes.get(i).getType(), actualFileTypes.get(i).getType());
      assertEquals(expectedFileTypes.get(i).getComment(), actualFileTypes.get(i).getComment());
      assertEquals(expectedFileTypes.get(i).getUse(), actualFileTypes.get(i).getUse());
    }
  }
}
