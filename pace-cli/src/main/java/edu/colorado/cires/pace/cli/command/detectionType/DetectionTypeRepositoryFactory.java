package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.sqlite.DetectionTypeSQLiteDatastore;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class DetectionTypeRepositoryFactory {

  public static DetectionTypeRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeRepository(
      new DetectionTypeJsonDatastore(
          datastoreDirectory, objectMapper
      )
    );
  }
  
  public static DetectionTypeRepository createSQLiteRepository(Path sqliteFile) {
    return new DetectionTypeRepository(
        new DetectionTypeSQLiteDatastore(sqliteFile)
    );
  }

}
