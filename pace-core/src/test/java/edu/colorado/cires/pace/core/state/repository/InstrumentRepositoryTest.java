package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InstrumentRepositoryTest extends CrudRepositoryTest<Instrument, String> {

  @Override
  protected UUIDProvider<Instrument> getUUIDPRovider() {
    return Instrument::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Instrument, String> getUniqueFieldProvider() {
    return Instrument::getName;
  }

  @Override
  protected UUIDSetter<Instrument> getUUIDSetter() {
    return Instrument::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Instrument, String> getUniqueFieldSetter() {
    return Instrument::setName;
  }
  
  private static final FileTypeRepository fileTypeRepository = mock(FileTypeRepository.class);
  static {
    try {
      when(fileTypeRepository.getByUUID(any())).thenReturn(new FileType());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected CRUDRepository<Instrument, String> createRepository() {
    return new InstrumentRepository(createDatastore(), fileTypeRepository);
  }

  @Override
  protected Instrument createNewObject(int suffix) {
    Instrument instrument = new Instrument();
    instrument.setName(String.format("name-%s", suffix));
    instrument.setUse(true);
    
    FileType fileType1 = new FileType();
    fileType1.setUUID(UUID.randomUUID());
    fileType1.setType(String.format("file-type-%s", suffix));
    
    FileType fileType2 = new FileType();
    fileType2.setUUID(UUID.randomUUID());
    fileType2.setType(String.format("file-type-%s", suffix));
    
    instrument.setFileTypes(List.of(
        fileType1, fileType2
    ));
    
    return instrument;
  }

  @Override
  protected void assertObjectsEqual(Instrument expected, Instrument actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getFileTypes(), actual.getFileTypes());
  }
  
  @Test
  void testFileTypeDoesNotExist() throws Exception {
    Instrument instrument = createNewObject(1);
    when(fileTypeRepository.getByUUID(instrument.getFileTypes().get(0).getUUID())).thenThrow(new NotFoundException("Not found"));
    when(fileTypeRepository.getByUUID(instrument.getFileTypes().get(1).getUUID())).thenReturn(instrument.getFileTypes().get(1));
    
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(instrument));
    assertEquals(String.format(
        "File type does not exist: %s", instrument.getFileTypes().get(0).getType()
    ), exception.getMessage());
  }
}
