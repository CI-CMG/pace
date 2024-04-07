package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.PersonController;
import edu.colorado.cires.pace.core.state.repository.PersonRepository;
import edu.colorado.cires.pace.core.state.service.PersonService;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class PersonControllerFactory {
  
  private static PersonJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static PersonRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static PersonService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static PersonController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
