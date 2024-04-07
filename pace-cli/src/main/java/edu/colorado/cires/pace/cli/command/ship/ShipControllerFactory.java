package edu.colorado.cires.pace.cli.command.ship;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.ShipController;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ShipControllerFactory {

  public static ShipController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipController(
        new ShipJsonDatastore(
            datastoreDirectory,
            objectMapper
        )
    );
  }

}
