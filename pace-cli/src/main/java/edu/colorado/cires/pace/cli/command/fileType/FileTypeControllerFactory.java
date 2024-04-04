package edu.colorado.cires.pace.cli.command.fileType;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.FileTypeController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.FileTypeRepository;
import edu.colorado.cires.pace.core.service.FileTypeService;
import edu.colorado.cires.pace.data.FileType;
import edu.colorado.cires.pace.datastore.json.FileTypeJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public final class FileTypeControllerFactory {
  
  private static Validator<FileType> createValidator() {
    return (fileType) -> {
      Set<ConstraintViolation> violations = new HashSet<>(0);
      
      if (StringUtils.isBlank(fileType.getType())) {
        violations.add(new ConstraintViolation(
            "type", "type must not be blank"
        ));
      }
      
      return violations;
    };
  }
  
  private static FileTypeJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  public static FileTypeRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static FileTypeService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static FileTypeController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new FileTypeController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
