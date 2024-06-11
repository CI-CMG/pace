package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class SampleRateTranslator {
  
  private final TimeTranslator startTimeTranslator;
  private final TimeTranslator endTimeTranslator;
  private final String sampleRate;
  private final String sampleBits;

}
