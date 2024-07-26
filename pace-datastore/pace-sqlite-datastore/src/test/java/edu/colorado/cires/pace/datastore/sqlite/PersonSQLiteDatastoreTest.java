package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.contact.person.Person;
import java.nio.file.Path;

class PersonSQLiteDatastoreTest extends SQLiteDatastoreTest<Person> {

  @Override
  protected String getDBFileName() {
    return "localData.sqlite";
  }

  @Override
  protected SQLiteDatastore<Person> createDatastore(Path dbPath) {
    return new PersonSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 39;
  }

  @Override
  protected void verifyObject(Person object) {
    assertNotNull(object.getUuid());
    assertNotNull(object.getName());
  }
}
