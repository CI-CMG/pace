package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Project;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ProjectValidator extends BaseValidator<Project> {

  @Override
  public Set<ConstraintViolation> runValidation(Project object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getName())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
