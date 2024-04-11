package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.datastore.json.SeaJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class SeaRepositoryFactory {

  public static SeaRepository createRepository(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaRepository(
        new SeaJsonDatastore(
            datastorePath, objectMapper
        )
    );
  }

}
