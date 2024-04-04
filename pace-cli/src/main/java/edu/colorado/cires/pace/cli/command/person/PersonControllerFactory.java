package edu.colorado.cires.pace.cli.command.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.controller.PersonController;
import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.PersonRepository;
import edu.colorado.cires.pace.core.service.PersonService;
import edu.colorado.cires.pace.data.Person;
import edu.colorado.cires.pace.datastore.json.PersonJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

final class PersonControllerFactory {
  
  private static Validator<Person> createValidator() {
    return (person) -> {
      Set<ConstraintViolation> constraintViolations = new HashSet<>(0);
      
      if (StringUtils.isBlank(person.getName())) {
        constraintViolations.add(new ConstraintViolation(
            "name", "name must not be blank"
        ));
      }
      
      if (StringUtils.isBlank(person.getOrganization())) {
        constraintViolations.add(new ConstraintViolation(
            "organization", "organization must not be blank"
        ));
      }
      
      if (StringUtils.isBlank(person.getPosition())) {
        constraintViolations.add(new ConstraintViolation(
            "position", "position must not be blank"
        ));
      }
      
      return constraintViolations;
    };
  }
  
  private static PersonJsonDatastore createDatastore(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonJsonDatastore(
        datastoreDirectory, objectMapper
    );
  }
  
  private static PersonRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonRepository(
        createDatastore(datastoreDirectory, objectMapper)
    );
  }
  
  private static PersonService createService(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonService(
        createRepository(datastoreDirectory, objectMapper)
    );
  }
  
  public static PersonController createController(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new PersonController(
        createService(datastoreDirectory, objectMapper),
        createValidator()
    );
  }

}
