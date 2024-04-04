package edu.colorado.cires.pace.cli.command.ship;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.ShipController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.ShipRepository;
import edu.colorado.cires.pace.core.service.ShipService;
import edu.colorado.cires.pace.data.Ship;
import edu.colorado.cires.pace.datastore.json.ShipJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class ShipControllerFactory {
  
  private static Validator<Ship> createValidator() {
    return (ship) -> {
      Set<ConstraintViolation> constraintViolations = new HashSet<>(0);
      if (StringUtils.isBlank(ship.getName())) {
        constraintViolations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      return constraintViolations;
    };
  }
  
  private static ShipJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipJsonDatastore(datastoreDirectory, objectMapper);
  }
  
  private static ShipRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static ShipService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static ShipController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ShipController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
