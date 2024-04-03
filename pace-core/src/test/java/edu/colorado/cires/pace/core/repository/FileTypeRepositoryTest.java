package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.FileType;

class FileTypeRepositoryTest extends CrudRepositoryTest<FileType, String> {

  @Override
  protected UUIDProvider<FileType> getUUIDPRovider() {
    return FileType::getUUID;
  }

  @Override
  protected UniqueFieldProvider<FileType, String> getUniqueFieldProvider() {
    return FileType::getType;
  }

  @Override
  protected UUIDSetter<FileType> getUUIDSetter() {
    return FileType::setUUID;
  }

  @Override
  protected UniqueFieldSetter<FileType, String> getUniqueFieldSetter() {
    return FileType::setType;
  }

  @Override
  protected CRUDRepository<FileType, String> createRepository() {
    return new FileTypeRepository(createDatastore());
  }

  @Override
  protected FileType createNewObject(int suffix) {
    FileType fileType = new FileType();
    fileType.setType(String.format("type-%s", suffix));
    fileType.setComment(String.format("comment-%s", suffix));
    fileType.setUse(true);
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
