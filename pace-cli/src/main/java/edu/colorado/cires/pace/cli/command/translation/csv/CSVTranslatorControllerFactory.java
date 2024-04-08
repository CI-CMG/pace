package edu.colorado.cires.pace.cli.command.translation.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.CSVTranslatorController;
import edu.colorado.cires.pace.datastore.json.CSVTranslatorJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class CSVTranslatorControllerFactory {
  
  public static CSVTranslatorController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new CSVTranslatorController(
        new CSVTranslatorJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
