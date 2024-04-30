package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.Ship;
import java.nio.file.Path;

class ShipSQLiteDatastoreTest extends SQLiteDatastoreTest<Ship> {

  @Override
  protected SQLiteDatastore<Ship> createDatastore(Path dbPath) {
    return new ShipSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 9;
  }

  @Override
  protected void verifyObject(Ship object) {
    assertNull(object.getUuid());
    assertNotNull(object.getName());
  }
}
