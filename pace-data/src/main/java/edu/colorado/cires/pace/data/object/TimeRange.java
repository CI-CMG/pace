package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidTimeRange;
import java.time.LocalDateTime;

@ValidTimeRange
public interface TimeRange {
  LocalDateTime getStartTime();
  LocalDateTime getEndTime();
}
