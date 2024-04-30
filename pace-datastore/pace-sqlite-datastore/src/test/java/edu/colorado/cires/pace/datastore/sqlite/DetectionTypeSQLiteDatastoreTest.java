package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.DetectionType;
import java.nio.file.Path;

class DetectionTypeSQLiteDatastoreTest extends SQLiteDatastoreTest<DetectionType> {

  @Override
  protected SQLiteDatastore<DetectionType> createDatastore(Path dbPath) {
    return new DetectionTypeSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 24;
  }

  @Override
  protected void verifyObject(DetectionType object) {
    assertNull(object.getUuid());
    assertNotNull(object.getSource());
  }
}
