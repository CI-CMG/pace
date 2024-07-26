package edu.colorado.cires.pace.data.object.dataset.audio.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class SampleRate implements TimeRange {
  
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  @Positive
  private final Float sampleRate;
  @Positive
  private final Integer sampleBits;

}
