package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.DetectionTypeSQLiteDatastore;
import edu.colorado.cires.pace.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class DetectionTypeRepositoryFactory {
  
  public static DetectionTypeJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static DetectionTypeRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeRepository(
      createJsonDatastore(datastoreDirectory, objectMapper),
      PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static DetectionTypeRepository createSQLiteRepository(Path sqliteFile, Datastore<Package> packageDatastore) {
    return new DetectionTypeRepository(
        new DetectionTypeSQLiteDatastore(sqliteFile),
        packageDatastore
    );
  }

}
