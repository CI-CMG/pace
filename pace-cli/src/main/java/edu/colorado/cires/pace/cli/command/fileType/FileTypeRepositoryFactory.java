package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.sqlite.FileTypeSQLiteDatastore;
import edu.colorado.cires.pace.repository.FileTypeRepository;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class FileTypeRepositoryFactory {
  
  public static FileTypeJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static FileTypeRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeRepository(
        createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static FileTypeRepository createSQLiteRepository(Path sqliteFile) {
    return new FileTypeRepository(
        new FileTypeSQLiteDatastore(sqliteFile)
    );
  }

}
