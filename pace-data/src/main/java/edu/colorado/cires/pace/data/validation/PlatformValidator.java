package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Platform;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PlatformValidator extends BaseValidator<Platform> {

  @Override
  protected Set<ConstraintViolation> runValidation(Platform object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getName())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
