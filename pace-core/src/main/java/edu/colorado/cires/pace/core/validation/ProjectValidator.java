package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.Project;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ProjectValidator implements Validator<Project> {

  @Override
  public Set<ConstraintViolation> validate(Project object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.name())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
