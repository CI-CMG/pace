package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Position;
import java.util.HashSet;
import java.util.Set;

public class PositionValidator extends BaseValidator<Position> {

  @Override
  protected Set<ConstraintViolation> runValidation(Position object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    if (object.getX() == null) {
      violations.add(new ConstraintViolation(
          "x", "x must be defined"
      ));
    }
    
    if (object.getY() == null) {
      violations.add(new ConstraintViolation(
          "y", "y must be defined"
      ));
    }
    
    if (object.getZ() == null) {
      violations.add(new ConstraintViolation(
          "z", "z must be defined"
      ));
    }
    
    return violations;
  }
}
