package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.SeaController;
import edu.colorado.cires.pace.core.state.repository.SeaRepository;
import edu.colorado.cires.pace.core.state.service.SeaService;
import edu.colorado.cires.pace.datastore.json.SeaJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class SeaControllerFactory {
  
  private static SeaJsonDatastore createDatastore(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaJsonDatastore(datastorePath, objectMapper);
  }
  
  private static SeaRepository createRepository(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaRepository(
        createDatastore(datastorePath, objectMapper)
    );
  }
  
  private static SeaService createService(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaService(
        createRepository(datastorePath, objectMapper)
    );
  }
  
  public static SeaController createController(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaController(
        createService(datastorePath, objectMapper)
    );
  }

}
