package edu.colorado.cires.pace.cli.command.translation.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.CSVTranslatorRepository;
import edu.colorado.cires.pace.datastore.json.CSVTranslatorJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class CSVTranslatorRepositoryFactory {
  
  public static CSVTranslatorRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new CSVTranslatorRepository(
        new CSVTranslatorJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
