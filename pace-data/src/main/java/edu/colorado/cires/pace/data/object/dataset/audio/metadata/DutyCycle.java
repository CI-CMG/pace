package edu.colorado.cires.pace.data.object.dataset.audio.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * DutyCycle holds the values which make up a duty cycle including startTime,
 * endTime, duration, and interval
 */
@Data
@Builder
@Jacksonized
public class DutyCycle implements TimeRange {
  
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final Float duration;
  private final Float interval;
  
}
