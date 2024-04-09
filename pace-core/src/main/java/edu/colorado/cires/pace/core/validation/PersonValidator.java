package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.Person;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PersonValidator implements Validator<Person> {

  @Override
  public Set<ConstraintViolation> validate(Person object) {
    Set<ConstraintViolation> constraintViolations = new HashSet<>(0);

    if (StringUtils.isBlank(object.name())) {
      constraintViolations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    if (StringUtils.isBlank(object.organization())) {
      constraintViolations.add(new ConstraintViolation(
          "organization", "organization must not be blank"
      ));
    }

    if (StringUtils.isBlank(object.position())) {
      constraintViolations.add(new ConstraintViolation(
          "position", "position must not be blank"
      ));
    }

    return constraintViolations;
  }
}
