package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class SampleRateTranslator {
  
  private final String startTime;
  private final String endTime;
  private final String sampleRate;
  private final String sampleBits;

}
