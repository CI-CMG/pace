package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.PersonRepository;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class PersonRepositoryFactory {

  public static PersonRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonRepository(
        new PersonJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
