package edu.colorado.cires.pace.core.validation;

import edu.colorado.cires.pace.data.Organization;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class OrganizationValidator implements Validator<Organization> {

  @Override
  public Set<ConstraintViolation> validate(Organization object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.name())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
