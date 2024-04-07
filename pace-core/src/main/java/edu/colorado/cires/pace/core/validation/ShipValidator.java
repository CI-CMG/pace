package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.Ship;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ShipValidator implements Validator<Ship> {

  @Override
  public Set<ConstraintViolation> validate(Ship object) {
    Set<ConstraintViolation> constraintViolations = new HashSet<>(0);
    if (StringUtils.isBlank(object.getName())) {
      constraintViolations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }
    return constraintViolations;
  }
}
