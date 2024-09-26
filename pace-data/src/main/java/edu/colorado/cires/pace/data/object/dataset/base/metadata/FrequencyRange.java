package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.validation.ValidFrequencyRange;
import jakarta.validation.constraints.Positive;

/**
 * FrequencyRange provides getters for max and min frequency
 */
@ValidFrequencyRange
public interface FrequencyRange {

  /**
   * Returns min frequency
   * @return Float min frequency
   */
  @Positive
  Float getMinFrequency();

  /**
   * Returns max frequency
   * @return Float max frequency
   */
  @Positive
  Float getMaxFrequency();

}
