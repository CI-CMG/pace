package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.sqlite.PersonSQLiteDatastore;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class PersonRepositoryFactory {

  public static PersonRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonRepository(
        new PersonJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }
  
  public static PersonRepository createSQLiteRepository(Path sqliteFile) {
    return new PersonRepository(
        new PersonSQLiteDatastore(sqliteFile)
    );
  }

}
