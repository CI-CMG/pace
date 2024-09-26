package edu.colorado.cires.pace.data.object.dataset.audio.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * Channel holds the values which make up a channel including startTime,
 * endTime, sampleRates, dutyCycles, and gains
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class Channel<T> implements TimeRange {

  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  @Builder.Default
  private final List<SampleRate> sampleRates = Collections.emptyList();
  @Builder.Default
  private final List<DutyCycle> dutyCycles = Collections.emptyList();
  @Builder.Default
  private final List<Gain> gains = Collections.emptyList();

}
