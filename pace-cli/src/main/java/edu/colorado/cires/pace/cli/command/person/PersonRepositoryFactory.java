package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.PersonSQLiteDatastore;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class PersonRepositoryFactory {
  
  public static PersonJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static PersonRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonRepository(
        createJsonDatastore(datastoreDirectory, objectMapper),
        PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static PersonRepository createSQLiteRepository(Path sqliteFile, Datastore<Package> packageDatastore) {
    return new PersonRepository(
        new PersonSQLiteDatastore(sqliteFile),
        packageDatastore
    );
  }

}
