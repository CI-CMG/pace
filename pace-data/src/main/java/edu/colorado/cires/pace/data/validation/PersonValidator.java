package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Person;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PersonValidator extends BaseValidator<Person> {

  @Override
  protected Set<ConstraintViolation> runValidation(Person object) {
    Set<ConstraintViolation> constraintViolations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getName())) {
      constraintViolations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    if (StringUtils.isBlank(object.getOrganization())) {
      constraintViolations.add(new ConstraintViolation(
          "organization", "organization must not be blank"
      ));
    }

    if (StringUtils.isBlank(object.getPosition())) {
      constraintViolations.add(new ConstraintViolation(
          "position", "position must not be blank"
      ));
    }

    return constraintViolations;
  }
}
