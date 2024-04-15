package edu.colorado.cires.pace.cli.command.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.json.SensorJsonDatastore;
import edu.colorado.cires.pace.repository.SensorRepository;
import java.io.IOException;
import java.nio.file.Path;

final class SensorRepositoryFactory {
  
  public static SensorRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new SensorRepository(
        new SensorJsonDatastore(datastoreDirectory, objectMapper)
    );
  } 

}
