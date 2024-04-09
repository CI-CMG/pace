package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InstrumentRepositoryTest extends CrudRepositoryTest<Instrument> {
  
  private static final Datastore<FileType> fileTypeRepository = mock(Datastore.class);
  static {
    try {
      when(fileTypeRepository.findByUUID(any())).thenReturn(Optional.of(new FileType(
          UUID.randomUUID(),
          UUID.randomUUID().toString(),
          "comment"
      )));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected CRUDRepository<Instrument> createRepository() {
    return new InstrumentRepository(createDatastore(), fileTypeRepository);
  }

  @Override
  protected Instrument createNewObject(int suffix) {
    
    FileType fileType1 = new FileType(
        UUID.randomUUID(),
        String.format("file-type-1-%s", suffix),
        "comment"
    );
    
    FileType fileType2 = new FileType(
        UUID.randomUUID(),
        String.format("file-type-2-%s", suffix),
        "comment"
    );
    
    return new Instrument(
        null,
        String.format("name-%s", suffix),
        List.of(
            fileType1, fileType2
        )
    );
  }

  @Override
  protected Instrument copyWithUpdatedUniqueField(Instrument object, String uniqueField) {
    return new Instrument(
        object.uuid(),
        uniqueField,
        object.fileTypes()
    );
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual, boolean checkUUID) {
    assertEquals(expected.name(), actual. name());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
    assertEquals(expected.fileTypes(), actual.fileTypes());
  }
  
  @Test
  void testFileTypeDoesNotExist() throws Exception {
    Instrument instrument = createNewObject(1);
    when(fileTypeRepository.findByUUID(instrument.fileTypes().get(0).uuid())).thenReturn(Optional.empty());
    when(fileTypeRepository.findByUUID(instrument.fileTypes().get(1).uuid())).thenReturn(Optional.of(instrument.fileTypes().get(1)));
    
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(instrument));
    assertEquals(String.format(
        "File type does not exist: %s", instrument.fileTypes().get(0).type()
    ), exception.getMessage());
  }
}
