package edu.colorado.cires.pace.cli.command.detectionType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.DetectionTypeController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.core.service.DetectionTypeService;
import edu.colorado.cires.pace.data.DetectionType;
import edu.colorado.cires.pace.datastore.json.DetectionTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class DetectionTypeControllerFactory {
  
  private static Validator<DetectionType> createValidator() {
    return (detectionType) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(detectionType.getScienceName())) {
        violations.add(new ConstraintViolation(
            "scienceName", "scienceName must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
  private static DetectionTypeJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeJsonDatastore(
        datastoreDirectory,
        objectMapper
    );
  }
  
  private static DetectionTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static DetectionTypeService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static DetectionTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new DetectionTypeController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
