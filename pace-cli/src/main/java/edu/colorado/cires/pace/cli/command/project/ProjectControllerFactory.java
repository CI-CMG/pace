package edu.colorado.cires.pace.cli.command.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.ProjectController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.ProjectRepository;
import edu.colorado.cires.pace.core.service.ProjectService;
import edu.colorado.cires.pace.data.Project;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class ProjectControllerFactory {
  
  private static Validator<Project> createValidator() {
    return (project) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(project.getName())) {
        violations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
  private static ProjectJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static ProjectRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static ProjectService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectService(
      createRepository(datastoreDirectory, objectMapper)  
    );
  }
  
  public static ProjectController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
