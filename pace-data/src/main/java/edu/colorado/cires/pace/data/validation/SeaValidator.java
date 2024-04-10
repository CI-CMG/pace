package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Sea;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class SeaValidator extends BaseValidator<Sea> {

  @Override
  protected Set<ConstraintViolation> runValidation(Sea object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getName())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
