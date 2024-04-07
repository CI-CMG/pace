package edu.colorado.cires.pace.cli.command.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.PlatformController;
import edu.colorado.cires.pace.core.state.repository.PlatformRepository;
import edu.colorado.cires.pace.core.state.service.PlatformService;
import edu.colorado.cires.pace.datastore.json.PlatformJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class PlatformControllerFactory {
  
  private static PlatformJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static PlatformRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static PlatformService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static PlatformController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
