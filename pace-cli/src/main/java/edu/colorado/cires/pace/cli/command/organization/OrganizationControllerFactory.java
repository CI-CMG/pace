package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.controller.OrganizationController;
import edu.colorado.cires.pace.core.state.repository.OrganizationRepository;
import edu.colorado.cires.pace.core.state.service.OrganizationService;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

final class OrganizationControllerFactory {
  
  private static OrganizationJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static OrganizationRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static OrganizationService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static OrganizationController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new OrganizationController(
        createService(datastoreDirectory, objectMapper)
    );
  }
  
}
