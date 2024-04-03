package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.data.Instrument;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
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
    when(fileTypeRepository.findByUUID(any())).thenReturn(Optional.of(new FileType()));
  }

  @Override
  protected CRUDRepository<Instrument, String> createRepository() {
    return new InstrumentRepository(fileTypeRepository) {
      @Override
      protected Instrument saveInstrument(Instrument instrument) {
        return saveObject(instrument);
      }

      @Override
      public Stream<Instrument> findAll() {
        return findAllObjects();
      }

      @Override
      protected Instrument delete(Instrument object) {
        return deleteObject(object);
      }

      @Override
      protected Optional<Instrument> findByUUID(UUID uuid) {
        return findObjectByUUID(uuid);
      }

      @Override
      protected Optional<Instrument> findByUniqueField(String uniqueField) {
        return findObjectByUniqueField(uniqueField);
      }
    };
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
  void testFileTypeDoesNotExist() {
    Instrument instrument = createNewObject(1);
    when(fileTypeRepository.findByUUID(instrument.getFileTypes().get(0).getUUID())).thenReturn(Optional.empty());
    when(fileTypeRepository.findByUUID(instrument.getFileTypes().get(1).getUUID())).thenReturn(Optional.of(
        instrument.getFileTypes().get(1)
    ));
    
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.save(instrument));
    assertEquals(String.format(
        "File type does not exist: %s", instrument.getFileTypes().get(0).getType()
    ), exception.getMessage());
  }
}
