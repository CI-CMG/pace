package edu.colorado.cires.pace.cli.command.translation.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.ExcelTranslatorRepository;
import edu.colorado.cires.pace.datastore.json.ExcelTranslatorJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ExcelTranslatorRepositoryFactory {
  
  public static ExcelTranslatorRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ExcelTranslatorRepository(
        new ExcelTranslatorJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
