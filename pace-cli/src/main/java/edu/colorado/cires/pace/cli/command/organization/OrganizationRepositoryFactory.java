package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.OrganizationsSQLiteDatastore;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class OrganizationRepositoryFactory {
  
  public static OrganizationJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static OrganizationRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationRepository(
        createJsonDatastore(datastoreDirectory, objectMapper),
        PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static OrganizationRepository createSQLiteRepository(Path sqliteFile, Datastore<Package> packageDatastore) {
    return new OrganizationRepository(
        new OrganizationsSQLiteDatastore(sqliteFile),
        packageDatastore
    );
  }
  
}
