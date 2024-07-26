package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.dataset.base.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.SeaSQLiteDatastore;
import edu.colorado.cires.pace.repository.SeaRepository;
import edu.colorado.cires.pace.datastore.json.SeaJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class SeaRepositoryFactory {
  
  public static SeaJsonDatastore createJsonDatastore(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaJsonDatastore(
        datastorePath, objectMapper
    );
  }

  public static SeaRepository createJsonRepository(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaRepository(
        createJsonDatastore(datastorePath, objectMapper),
        PackageRepositoryFactory.createJsonDatastore(datastorePath, objectMapper)
    );
  }
  
  public static SeaRepository createSQLiteRepository(Path sqliteFile, Datastore<Package> packageDatastore) {
    return new SeaRepository(
        new SeaSQLiteDatastore(sqliteFile),
        packageDatastore
    );
  }

}
