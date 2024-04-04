package edu.colorado.cires.pace.cli.command.platform;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.PlatformController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.PlatformRepository;
import edu.colorado.cires.pace.core.service.PlatformService;
import edu.colorado.cires.pace.data.Platform;
import edu.colorado.cires.pace.datastore.json.PlatformJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class PlatformControllerFactory {
  
  private static Validator<Platform> createValidator() {
    return (platform) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(platform.getName())) {
        violations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
  private static PlatformJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static PlatformRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static PlatformService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static PlatformController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PlatformController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
