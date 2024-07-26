package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.fileType.FileType;
import java.nio.file.Path;

class FileTypeSQLiteDatastoreTest extends SQLiteDatastoreTest<FileType> {

  @Override
  protected SQLiteDatastore<FileType> createDatastore(Path dbPath) {
    return new FileTypeSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 5;
  }

  @Override
  protected void verifyObject(FileType object) {
    assertNull(object.getUuid());
    assertNull(object.getComment());
    assertNotNull(object.getType());
  }
}
