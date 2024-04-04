package edu.colorado.cires.pace.cli.command.sea;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.SeaController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.SeaRepository;
import edu.colorado.cires.pace.core.service.SeaService;
import edu.colorado.cires.pace.data.Sea;
import edu.colorado.cires.pace.datastore.json.SeaJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class SeaControllerFactory {
  
  private static Validator<Sea> createValidator() {
    return (sea) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(sea.getName())) {
        violations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
  private static SeaJsonDatastore createDatastore(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaJsonDatastore(datastorePath, objectMapper);
  }
  
  private static SeaRepository createRepository(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaRepository(
        createDatastore(datastorePath, objectMapper)
    );
  }
  
  private static SeaService createService(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaService(
        createRepository(datastorePath, objectMapper)
    );
  }
  
  public static SeaController createController(Path datastorePath, ObjectMapper objectMapper) throws IOException {
    return new SeaController(
        createService(datastorePath, objectMapper), 
        createValidator()
    );
  }

}
