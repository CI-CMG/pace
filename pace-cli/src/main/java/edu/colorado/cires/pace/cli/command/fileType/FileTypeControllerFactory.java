package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.FileTypeController;
import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.datastore.json.JsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class FileTypeControllerFactory {
  
  public static Datastore<FileType> createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new JsonDatastore<>(
        datastoreDirectory, objectMapper, FileType.class
    ) {};
  }

  public static FileTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeController(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }

}
