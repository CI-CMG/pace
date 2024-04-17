package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidFrequencyRange;
import jakarta.validation.constraints.Positive;

@ValidFrequencyRange
public interface FrequencyRange {
  @Positive
  Float getMinFrequency();
  @Positive
  Float getMaxFrequency();

}
