package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.DetectionTypeController;
import edu.colorado.cires.pace.core.state.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.core.state.service.DetectionTypeService;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class DetectionTypeControllerFactory {
  
  private static DetectionTypeJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeJsonDatastore(
        datastoreDirectory,
        objectMapper
    );
  }
  
  private static DetectionTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static DetectionTypeService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static DetectionTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
