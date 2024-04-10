package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.data.object.Instrument;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InstrumentRepositoryTest extends CrudRepositoryTest<Instrument> {
  
  private static final Datastore<FileType> fileTypeRepository = mock(Datastore.class);
  static {
    try {
      when(fileTypeRepository.findByUUID(any())).thenReturn(Optional.of(FileType.builder()
              .uuid(UUID.randomUUID())
              .type(UUID.randomUUID().toString())
              .comment("comment")
          .build()));
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
    FileType fileType1 = FileType.builder()
        .uuid(UUID.randomUUID())
        .type(String.format("file-type-1-%s", suffix))
        .comment("comment")
        .build();
    
    FileType fileType2 = FileType.builder()
        .uuid(UUID.randomUUID())
        .type(String.format("file-type-2-%s", suffix))
        .comment("comment")
        .build();
    
    return Instrument.builder()
        .name(String.format("name-%s", suffix))
        .fileTypes(List.of(
            fileType1, fileType2
        )).build();
  }

  @Override
  protected Instrument copyWithUpdatedUniqueField(Instrument object, String uniqueField) {
    return Instrument.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .fileTypes(object.getFileTypes())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getFileTypes(), actual.getFileTypes());
  }
  
  @Test
  void testFileTypeDoesNotExist() throws Exception {
    Instrument instrument = createNewObject(1);
    when(fileTypeRepository.findByUUID(instrument.getFileTypes().get(0).getUuid())).thenReturn(Optional.empty());
    when(fileTypeRepository.findByUUID(instrument.getFileTypes().get(1).getUuid())).thenReturn(Optional.of(instrument.getFileTypes().get(1)));
    
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(instrument));
    assertEquals(String.format(
        "File type does not exist: %s", instrument.getFileTypes().get(0).getType()
    ), exception.getMessage());
  }
}
