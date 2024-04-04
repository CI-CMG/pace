package edu.colorado.cires.pace.cli.command.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.OrganizationController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.OrganizationRepository;
import edu.colorado.cires.pace.core.service.OrganizationService;
import edu.colorado.cires.pace.data.Organization;
import edu.colorado.cires.pace.datastore.json.OrganizationJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class OrganizationControllerFactory {
  
  private static Validator<Organization> createValidator() {
    return (organization) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(organization.getName())) {
        violations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
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
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }
  
}
