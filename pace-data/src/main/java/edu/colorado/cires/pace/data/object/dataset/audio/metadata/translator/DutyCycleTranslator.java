package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * DutyCycleTranslator holds header names for fields of a duty cycle within a channel
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class DutyCycleTranslator {

  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final String duration;
  private final String interval;

}
