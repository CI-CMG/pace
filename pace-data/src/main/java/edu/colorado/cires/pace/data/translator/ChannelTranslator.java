package edu.colorado.cires.pace.data.translator;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ChannelTranslator {
  
  private final String sensor;
  private final String startTime;
  private final String endTime;
  private final List<SampleRateTranslator> sampleRateTranslators;
  private final List<DutyCycleTranslator> dutyCycleTranslators;
  private final List<GainTranslator> gainTranslators;

}
