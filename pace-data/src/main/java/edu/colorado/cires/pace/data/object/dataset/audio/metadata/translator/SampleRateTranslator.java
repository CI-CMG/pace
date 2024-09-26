package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * SampleRateTranslator holds header names for fields of a sample rate
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class SampleRateTranslator {
  
  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final String sampleRate;
  private final String sampleBits;

}
