package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.platform.Platform;
import java.nio.file.Path;

class PlatformSQLiteDatastoreTest extends SQLiteDatastoreTest<Platform> {

  @Override
  protected SQLiteDatastore<Platform> createDatastore(Path dbPath) {
    return new PlatformSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 6;
  }

  @Override
  protected void verifyObject(Platform object) {
    assertNull(object.getUuid());
    assertNotNull(object.getName());
  }
}
