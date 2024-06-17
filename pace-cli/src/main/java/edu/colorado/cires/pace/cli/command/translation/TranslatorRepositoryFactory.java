package edu.colorado.cires.pace.cli.command.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.json.TranslatorJsonDatastore;
import edu.colorado.cires.pace.repository.TranslatorRepository;
import java.io.IOException;
import java.nio.file.Path;

public class TranslatorRepositoryFactory {
  
  public static TranslatorRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new TranslatorRepository(
        new TranslatorJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
