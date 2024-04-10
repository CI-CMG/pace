package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.FileType;

class FileTypeRepositoryTest extends CrudRepositoryTest<FileType> {

  @Override
  protected CRUDRepository<FileType> createRepository() {
    return new FileTypeRepository(createDatastore());
  }

  @Override
  protected FileType createNewObject(int suffix) {
    return FileType.builder()
        .type(String.format("type-%s", suffix))
        .comment(String.format("comment-%s", suffix))
        .build();
  }

  @Override
  protected FileType copyWithUpdatedUniqueField(FileType object, String uniqueField) {
    return FileType.builder()
        .uuid(object.getUuid())
        .type(uniqueField)
        .comment(object.getComment())
        .build();
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual, boolean checkUUID) {
    assertEquals(expected.getComment(), actual.getComment());
    assertEquals(expected.getType(), actual.getType());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }
}
