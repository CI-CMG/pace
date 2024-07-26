package edu.colorado.cires.pace.datastore.sqlite;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.colorado.cires.pace.data.object.contact.organization.Organization;
import java.nio.file.Path;

class OrganizationSQLiteDatastoreTest extends SQLiteDatastoreTest<Organization> {

  @Override
  protected String getDBFileName() {
    return "localData.sqlite";
  }

  @Override
  protected SQLiteDatastore<Organization> createDatastore(Path dbPath) {
    return new OrganizationsSQLiteDatastore(dbPath);
  }

  @Override
  protected int getExpectedRowCount() {
    return 41;
  }

  @Override
  protected void verifyObject(Organization object) {
    assertNotNull(object.getUuid());
    assertNotNull(object.getName());
    assertNotNull(object.getCountry());
  }
}
