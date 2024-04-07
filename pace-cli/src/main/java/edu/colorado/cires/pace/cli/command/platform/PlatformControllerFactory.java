package edu.colorado.cires.pace.cli.command.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.PlatformController;
import edu.colorado.cires.pace.datastore.json.PlatformJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class PlatformControllerFactory {

  public static PlatformController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformController(
        new PlatformJsonDatastore(
            datastoreDirectory,
            objectMapper
        )
    );
  }

}
