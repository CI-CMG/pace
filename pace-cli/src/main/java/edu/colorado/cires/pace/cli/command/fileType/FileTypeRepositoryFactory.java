package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class FileTypeRepositoryFactory {
  
  public static FileTypeJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static FileTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }

}
