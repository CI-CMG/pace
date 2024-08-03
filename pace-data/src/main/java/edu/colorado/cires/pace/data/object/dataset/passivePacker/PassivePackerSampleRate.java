package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerSampleRate extends PassivePackerTimeRange {
  
  private final String sampleRate;
  private final String sampleBits;

}
