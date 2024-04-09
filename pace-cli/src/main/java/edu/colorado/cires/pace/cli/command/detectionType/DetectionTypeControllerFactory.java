package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.DetectionTypeController;
import edu.colorado.cires.pace.data.DetectionType;
import edu.colorado.cires.pace.datastore.json.JsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class DetectionTypeControllerFactory {

  public static DetectionTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeController(
      new JsonDatastore<>(
          datastoreDirectory, objectMapper, DetectionType.class
      )
    );
  }

}
