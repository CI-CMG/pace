package edu.colorado.cires.pace.cli.command.dataset;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Package;
import edu.colorado.cires.pace.datastore.json.PackageJsonDatastore;
import edu.colorado.cires.pace.repository.CRUDRepository;
import edu.colorado.cires.pace.repository.PackageRepository;
import java.io.IOException;
import java.nio.file.Path;

final class PackageRepositoryFactory {
  
  public static CRUDRepository<Package> createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PackageRepository(
        new PackageJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }

}
