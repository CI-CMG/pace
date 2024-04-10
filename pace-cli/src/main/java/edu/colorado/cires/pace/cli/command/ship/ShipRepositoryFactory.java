package edu.colorado.cires.pace.cli.command.ship;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.ShipRepository;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ShipRepositoryFactory {

  public static ShipRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipRepository(
        new ShipJsonDatastore(
            datastoreDirectory, objectMapper
            )
    );
  }

}
