package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.OrganizationController;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class OrganizationControllerFactory {

  public static OrganizationController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationController(
        new OrganizationJsonDatastore(
            datastoreDirectory,
            objectMapper
        )
    );
  }
  
}
