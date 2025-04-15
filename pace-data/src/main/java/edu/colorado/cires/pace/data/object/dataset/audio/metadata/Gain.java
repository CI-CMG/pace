package edu.colorado.cires.pace.data.object.dataset.audio.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * DutyCycle holds the values which make up a duty cycle including startTime,
 * endTime, and gain
 */
@Data
@Builder
@Jacksonized
public class Gain implements TimeRange {
  @NotEmpty
  private final LocalDateTime startTime;
  @NotEmpty
  private final LocalDateTime endTime;
  @NotEmpty
  private final Float gain;
  
}
