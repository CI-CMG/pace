package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class DutyCycle {
  
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final Float duration;
  private final Float interval;

  @Builder
  @Jacksonized
  private DutyCycle(LocalDateTime startTime, LocalDateTime endTime, Float duration, Float interval) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.duration = duration;
    this.interval = interval;
  }
}
