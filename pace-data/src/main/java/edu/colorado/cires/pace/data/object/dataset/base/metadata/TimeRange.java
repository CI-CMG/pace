package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.validation.ValidTimeRange;
import java.time.LocalDateTime;

@ValidTimeRange
public interface TimeRange {
  LocalDateTime getStartTime();
  LocalDateTime getEndTime();
}
