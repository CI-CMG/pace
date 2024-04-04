package edu.colorado.cires.pace.cli.command.instrument;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.cli.command.fileType.FileTypeControllerFactory;
import edu.colorado.cires.pace.core.controller.InstrumentController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.InstrumentRepository;
import edu.colorado.cires.pace.core.service.InstrumentService;
import edu.colorado.cires.pace.data.Instrument;
import edu.colorado.cires.pace.datastore.json.InstrumentJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class InstrumentControllerFactory {
  
  private static Validator<Instrument> createValidator() {
    return (instrument) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(instrument.getName())) {
        violations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
  private static InstrumentJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static InstrumentRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentRepository(
        createDatastore(datastoreDirectory, objectMapper),
        FileTypeControllerFactory.createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  private static InstrumentService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static InstrumentController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new InstrumentController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
