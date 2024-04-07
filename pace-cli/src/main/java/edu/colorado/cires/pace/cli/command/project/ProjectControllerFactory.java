package edu.colorado.cires.pace.cli.command.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.ProjectController;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ProjectControllerFactory {

  public static ProjectController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectController(
        new ProjectJsonDatastore(
            datastoreDirectory,
            objectMapper
        )
    );
  }

}
