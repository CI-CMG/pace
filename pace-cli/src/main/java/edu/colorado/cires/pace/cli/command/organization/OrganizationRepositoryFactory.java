package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.OrganizationRepository;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class OrganizationRepositoryFactory {

  public static OrganizationRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationRepository(
        new OrganizationJsonDatastore(
            datastoreDirectory, objectMapper
        )
    );
  }
  
}
