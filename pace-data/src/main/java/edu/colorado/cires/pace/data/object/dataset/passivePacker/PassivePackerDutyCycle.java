package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerDutyCycle extends PassivePackerTimeRange {
  
  private final String duration;
  private final String interval;

}