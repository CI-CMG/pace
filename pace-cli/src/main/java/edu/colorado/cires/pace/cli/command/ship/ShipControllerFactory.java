package edu.colorado.cires.pace.cli.command.ship;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.ShipController;
import edu.colorado.cires.pace.core.state.repository.ShipRepository;
import edu.colorado.cires.pace.core.state.service.ShipService;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class ShipControllerFactory {
  
  private static ShipJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipJsonDatastore(datastoreDirectory, objectMapper);
  }
  
  private static ShipRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static ShipService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static ShipController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
