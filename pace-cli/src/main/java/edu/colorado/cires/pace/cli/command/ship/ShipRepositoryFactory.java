package edu.colorado.cires.pace.cli.command.ship;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.ShipSQLiteDatastore;
import edu.colorado.cires.pace.repository.ShipRepository;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class ShipRepositoryFactory {
  
  public static ShipJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static ShipRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipRepository(
        new ShipJsonDatastore(
            datastoreDirectory, objectMapper
            ),
        PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static ShipRepository createSQLiteRepository(Path sqliteFile, Datastore<Package> packageDatastore) {
    return new ShipRepository(
        new ShipSQLiteDatastore(sqliteFile),
        packageDatastore
    );
  }

}
