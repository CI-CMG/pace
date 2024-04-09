package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.FileType;

class FileTypeRepositoryTest extends CrudRepositoryTest<FileType> {

  @Override
  protected CRUDRepository<FileType> createRepository() {
    return new FileTypeRepository(createDatastore());
  }

  @Override
  protected FileType createNewObject(int suffix) {
    return new FileType(
        null,
        String.format("type-%s", suffix),
        String.format("comment-%s", suffix)
    );
  }

  @Override
  protected FileType copyWithUpdatedUniqueField(FileType object, String uniqueField) {
    return new FileType(
        object.uuid(),
        uniqueField,
        object.comment()
    );
  }

  @Override
  protected void assertObjectsEqual(FileType expected, FileType actual, boolean checkUUID) {
    assertEquals(expected.comment(), actual.comment());
    assertEquals(expected.type(), actual.type());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
  }
}
