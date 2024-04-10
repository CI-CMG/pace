package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Instrument;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class InstrumentValidator extends BaseValidator<Instrument> {

  @Override
  public Set<ConstraintViolation> runValidation(Instrument object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);

    if (StringUtils.isBlank(object.getName())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank"
      ));
    }

    return violations;
  }
}
