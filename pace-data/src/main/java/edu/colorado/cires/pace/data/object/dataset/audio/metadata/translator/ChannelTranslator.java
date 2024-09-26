package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * ChannelTranslator holds header names for fields of channels
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class ChannelTranslator {

  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  @Builder.Default
  private final List<SampleRateTranslator> sampleRates = Collections.emptyList();
  @Builder.Default
  private final List<DutyCycleTranslator> dutyCycles = Collections.emptyList();
  @Builder.Default
  private final List<GainTranslator> gains = Collections.emptyList();

}
