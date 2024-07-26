package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.colorado.cires.pace.data.object.project.Project;
import java.nio.file.Path;

class ProjectSQLiteDatastoreTest extends SQLiteDatastoreTest<Project> {

  @Override
  protected String getDBFileName() {
    return "localData.sqlite";
  }

  @Override
  protected SQLiteDatastore<Project> createDatastore(Path dbPath) {
    return new ProjectSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 11;
  }

  @Override
  protected void verifyObject(Project object) {
    assertNull(object.getUuid());
    assertNotNull(object.getName());
  }
}
