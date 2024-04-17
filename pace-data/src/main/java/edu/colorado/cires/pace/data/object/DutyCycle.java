package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class DutyCycle implements TimeRange {
  
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final Float duration;
  private final Float interval;
  
}
