package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.Datastore;
import edu.colorado.cires.pace.core.state.FileTypeRepository;
import edu.colorado.cires.pace.data.object.FileType;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class FileTypeRepositoryFactory {
  
  public static Datastore<FileType> createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(
        datastoreDirectory, objectMapper
    ) {};
  }

  public static FileTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }

}
