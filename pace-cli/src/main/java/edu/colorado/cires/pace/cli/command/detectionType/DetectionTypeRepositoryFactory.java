package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class DetectionTypeRepositoryFactory {

  public static DetectionTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeRepository(
      new DetectionTypeJsonDatastore(
          datastoreDirectory, objectMapper
      )
    );
  }

}
