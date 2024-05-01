package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.datastore.sqlite.OrganizationsSQLiteDatastore;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class OrganizationRepositoryFactory {

  public static OrganizationRepository createJsonRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationRepository(
        new OrganizationJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }
  
  public static OrganizationRepository createSQLiteRepository(Path sqliteFile) {
    return new OrganizationRepository(
        new OrganizationsSQLiteDatastore(sqliteFile)
    );
  }
  
}
