package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.FileTypeRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.FileType;
import java.util.UUID;

class FileTypeServiceTest extends CrudServiceTest<FileType, String, FileTypeRepository> {

  @Override
  protected Class<FileTypeRepository> getRepositoryClass() {
    return FileTypeRepository.class;
  }

  @Override
  protected UniqueFieldProvider<FileType, String> getUniqueFieldProvider() {
    return FileType::getType;
  }

  @Override
  protected UUIDProvider<FileType> getUUIDProvider() {
    return FileType::getUUID;
  }

  @Override
  protected CRUDService<FileType, String> createService(FileTypeRepository repository) {
    return new FileTypeService(repository);
  }

  @Override
  protected FileType createNewObject() {
    FileType fileType = new FileType();
    fileType.setUUID(UUID.randomUUID());
    fileType.setUse(true);
    fileType.setType(UUID.randomUUID().toString());
    fileType.setComment(UUID.randomUUID().toString());
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