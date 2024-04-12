package edu.colorado.cires.pace.data.validation;

import edu.colorado.cires.pace.data.object.AudioSensor;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class AudioSensorValidator extends SensorValidator<AudioSensor> {

  @Override
  protected Set<ConstraintViolation> validateAdditionalFields(AudioSensor object) {
    Set<ConstraintViolation> violations = new HashSet<>(0);
    
    if (StringUtils.isBlank(object.getHydrophoneId())) {
      violations.add(ConstraintViolation.builder()
              .property("hydrophoneId")
              .message("hydrophoneId must not be blank")
          .build());
    }
    
    if (StringUtils.isBlank(object.getPreampId())) {
      violations.add(ConstraintViolation.builder()
              .property("preampId")
              .message("preampId must not be blank")
          .build());
    }
    
    return violations;
  }
}
