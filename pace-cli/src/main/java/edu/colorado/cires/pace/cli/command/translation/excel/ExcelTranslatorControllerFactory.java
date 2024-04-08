package edu.colorado.cires.pace.cli.command.translation.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.ExcelTranslatorController;
import edu.colorado.cires.pace.datastore.json.ExcelTranslatorJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ExcelTranslatorControllerFactory {
  
  public static ExcelTranslatorController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ExcelTranslatorController(
        new ExcelTranslatorJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
