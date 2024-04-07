package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.DetectionTypeController;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class DetectionTypeControllerFactory {

  public static DetectionTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeController(
      new DetectionTypeJsonDatastore(
          datastoreDirectory,
          objectMapper
      )
    );
  }

}
