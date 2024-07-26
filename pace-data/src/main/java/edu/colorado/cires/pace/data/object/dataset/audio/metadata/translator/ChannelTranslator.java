package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class ChannelTranslator {
  
  private final PackageSensorTranslator sensor;
  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final List<SampleRateTranslator> sampleRates;
  private final List<DutyCycleTranslator> dutyCycles;
  private final List<GainTranslator> gains;

}
