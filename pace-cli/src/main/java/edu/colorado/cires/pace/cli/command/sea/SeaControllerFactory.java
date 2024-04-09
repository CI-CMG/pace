package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.SeaController;
import edu.colorado.cires.pace.data.Sea;
import edu.colorado.cires.pace.datastore.json.JsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class SeaControllerFactory {

  public static SeaController createController(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaController(
        new JsonDatastore<>(
            datastorePath, objectMapper, Sea.class
        )
    );
  }

}
