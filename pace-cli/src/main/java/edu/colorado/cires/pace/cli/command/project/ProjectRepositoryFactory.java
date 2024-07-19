package edu.colorado.cires.pace.cli.command.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.dataset.PackageRepositoryFactory;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.sqlite.ProjectSQLiteDatastore;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class ProjectRepositoryFactory {
  
  public static ProjectJsonDatastore createJsonDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }

  public static ProjectRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectRepository(
        createJsonDatastore(datastoreDirectory, objectMapper),
        PackageRepositoryFactory.createJsonDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  public static ProjectRepository createSQLiteRepository(Path sqliteFile, Datastore<Package> packageDatastore) {
    return new ProjectRepository(
        new ProjectSQLiteDatastore(sqliteFile),
        packageDatastore
    );
  }

}
