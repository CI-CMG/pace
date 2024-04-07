package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.FileTypeController;
import edu.colorado.cires.pace.core.state.repository.FileTypeRepository;
import edu.colorado.cires.pace.core.state.service.FileTypeService;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class FileTypeControllerFactory {
  
  private static FileTypeJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  public static FileTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static FileTypeService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static FileTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeController(
        createService(datastoreDirectory, objectMapper)
    );
  }

}
