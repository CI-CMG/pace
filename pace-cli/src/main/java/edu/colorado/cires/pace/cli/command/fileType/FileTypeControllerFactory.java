package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.FileTypeController;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class FileTypeControllerFactory {
  
  public static FileTypeJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static FileTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeController(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }

}
