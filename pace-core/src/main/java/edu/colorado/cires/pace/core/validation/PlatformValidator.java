package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.Platform;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class PlatformValidator implements Validator<Platform> {

  @Override
  public Set<ConstraintViolation> validate(Platform object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.name())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
