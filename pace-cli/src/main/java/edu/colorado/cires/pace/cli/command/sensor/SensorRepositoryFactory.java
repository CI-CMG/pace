package edu.colorado.cires.pace.cli.command.sensor;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.datastore.json.SensorJsonDatastore;
import edu.colorado.cires.pace.repository.SensorRepository;
import java.io.IOException;
import java.nio.file.Path;

public final class SensorRepositoryFactory {
  
  public static SensorJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new SensorJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  public static SensorRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new SensorRepository(
        new SensorJsonDatastore(datastoreDirectory, objectMapper),
        PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  } 

}
