package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.PersonController;
import edu.colorado.cires.pace.data.Person;
import edu.colorado.cires.pace.datastore.json.JsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class PersonControllerFactory {

  public static PersonController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonController(
        new JsonDatastore<>(
            datastoreDirectory, objectMapper, Person.class
        )
    );
  }

}
