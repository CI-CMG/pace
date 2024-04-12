package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.OtherSensor;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class OtherSensorValidator extends SensorValidator<OtherSensor> {

  @Override
  protected Set<ConstraintViolation> validateAdditionalFields(OtherSensor object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    if (StringUtils.isBlank(object.getSensorType())) {
      violations.add(ConstraintViolation.builder()
              .property("sensorType")
              .message("sensorType must not be blank")
          .build());
    }
    
    if (StringUtils.isBlank(object.getProperties())) {
      violations.add(ConstraintViolation.builder()
              .property("properties")
              .message("properties must not be blank")
          .build());
    }
    
    return violations;
  }
}
