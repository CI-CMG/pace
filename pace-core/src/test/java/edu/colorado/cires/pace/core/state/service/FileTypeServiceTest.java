package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.FileTypeRepository;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;

class FileTypeServiceTest extends CrudServiceTest<FileType, FileTypeRepository> {

  @Override
  protected Class<FileTypeRepository> getRepositoryClass() {
    return FileTypeRepository.class;
  }

  @Override
  protected CRUDService<FileType> createService(FileTypeRepository repository) {
    return new FileTypeService(repository);
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
    assertEquals(expected.uuid(), actual.uuid());
  }
}
