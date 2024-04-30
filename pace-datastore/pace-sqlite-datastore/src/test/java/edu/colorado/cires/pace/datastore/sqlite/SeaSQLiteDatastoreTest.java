package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.Sea;
import java.nio.file.Path;

class SeaSQLiteDatastoreTest extends SQLiteDatastoreTest<Sea> {

  @Override
  protected SQLiteDatastore<Sea> createDatastore(Path dbPath) {
    return new SeaSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 101;
  }

  @Override
  protected void verifyObject(Sea object) {
    assertNull(object.getUuid());
    assertNotNull(object.getName());
  }
}
