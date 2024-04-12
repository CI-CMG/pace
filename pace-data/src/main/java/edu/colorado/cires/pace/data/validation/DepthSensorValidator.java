package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.DepthSensor;
import java.util.Set;

public class DepthSensorValidator extends SensorValidator<DepthSensor> {

  @Override
  protected Set<ConstraintViolation> validateAdditionalFields(DepthSensor object) {
    return Set.of();
  }
}
