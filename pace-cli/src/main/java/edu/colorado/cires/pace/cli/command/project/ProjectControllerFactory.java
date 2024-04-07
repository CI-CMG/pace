package edu.colorado.cires.pace.cli.command.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.ProjectController;
import edu.colorado.cires.pace.core.state.repository.ProjectRepository;
import edu.colorado.cires.pace.core.state.service.ProjectService;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ProjectControllerFactory {
  
  private static ProjectJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static ProjectRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static ProjectService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectService(
      createRepository(datastoreDirectory, objectMapper)  
    );
  }
  
  public static ProjectController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
