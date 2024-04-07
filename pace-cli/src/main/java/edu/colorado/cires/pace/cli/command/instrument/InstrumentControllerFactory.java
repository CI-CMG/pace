package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.InstrumentController;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class InstrumentControllerFactory {

  public static InstrumentController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentController(
        new InstrumentJsonDatastore(datastoreDirectory, objectMapper),
        new FileTypeJsonDatastore(datastoreDirectory, objectMapper)
    );
  }

}
