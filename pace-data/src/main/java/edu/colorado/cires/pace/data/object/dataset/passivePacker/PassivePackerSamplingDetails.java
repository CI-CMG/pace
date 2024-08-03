package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
@Jacksonized
public class PassivePackerSamplingDetails {
  
  private final List<PassivePackerSampleRate> sampling;
  private final List<PassivePackerGain> gain;
  private final List<PassivePackerDutyCycle> dutyCycle;

}
