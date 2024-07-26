package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

abstract class SQLiteDatastoreTest<O extends ObjectWithUniqueField> {
  
  protected abstract SQLiteDatastore<O> createDatastore(Path dbPath);
  protected abstract int getExpectedRowCount();
  protected abstract void verifyObject(O object);
  
  protected String getDBFileName() {
    return "sourceData.sqlite";
  }
  
  protected Path getDBPath() {
    return Paths.get("src").resolve("test").resolve("resources").resolve(getDBFileName()).toAbsolutePath();
  }
  
  private final SQLiteDatastore<O> datastore = createDatastore(getDBPath());

  @Test
  void findAll() throws DatastoreException {
    List<O> objects = datastore.findAll().toList();
    assertEquals(getExpectedRowCount(), objects.size());

    for (O object : objects) {
      verifyObject(object);
    }
  }

  @Test
  void save() {
    assertThrows(DatastoreException.class, () -> datastore.save(null));
  }

  @Test
  void delete() {
    assertThrows(DatastoreException.class, () -> datastore.delete(null));
  }

  @Test
  void findByUUID() {
    assertThrows(DatastoreException.class, () -> datastore.findByUUID(null));
  }

  @Test
  void findByUniqueField() {
    assertThrows(DatastoreException.class, () -> datastore.findByUniqueField(null));
  }

  @Test
  void getUniqueFieldName() {
    assertNull(datastore.getUniqueFieldName());
  }
  
  @Test
  void invalidDBFile() {
    Datastore<O> misconfiguredDatastore = createDatastore(Paths.get("target").resolve("db.sqlite"));
    Exception exception = assertThrows(DatastoreException.class, misconfiguredDatastore::findAll);
    assertEquals("Failed to list table rows", exception.getMessage());
  }
}
