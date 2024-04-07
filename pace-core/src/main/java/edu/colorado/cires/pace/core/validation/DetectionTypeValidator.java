package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.DetectionType;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class DetectionTypeValidator implements Validator<DetectionType> {

  @Override
  public Set<ConstraintViolation> validate(DetectionType object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getScienceName())) {
      violations.add(new ConstraintViolation(
          "scienceName", "scienceName must not be blank"
      ));
    }

    return violations;
  }
}
