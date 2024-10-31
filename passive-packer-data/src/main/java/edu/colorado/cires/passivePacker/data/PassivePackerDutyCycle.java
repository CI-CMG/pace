package edu.colorado.cires.passivePacker.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * PassivePackerDutyCycle extends PassivePackerTimeRange provides builder structure for
 * PassivePackerDutyCycle objects
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerDutyCycle extends PassivePackerTimeRange {
  
  private final String duration;
  private final String interval;

}
