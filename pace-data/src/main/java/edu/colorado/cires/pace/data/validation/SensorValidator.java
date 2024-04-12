package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.Position;
import edu.colorado.cires.pace.data.object.Sensor;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public abstract class SensorValidator<T extends Sensor> extends BaseValidator<T> {

  @Override
  protected Set<ConstraintViolation> runValidation(T object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    if (StringUtils.isBlank(object.getName())) {
      violations.add(new ConstraintViolation(
          "name", "name must not be blank" 
      ));
    }

    Position position = object.getPosition();
    if (position == null) {
      violations.add(new ConstraintViolation(
          "position", "position must be defined"
      ));
    } else {
      violations.addAll(
          new PositionValidator().runValidation(position).stream()
              .map(constraintViolation -> constraintViolation.toBuilder()
                  .property(String.format(
                      "position.%s", constraintViolation.getProperty()
                  ))
                  .build())
              .collect(Collectors.toSet())
      );
    }
    
    violations.addAll(validateAdditionalFields(object));
    
    return violations;
  }
  
  protected abstract Set<ConstraintViolation> validateAdditionalFields(T object);
}
