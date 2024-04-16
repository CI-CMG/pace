package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;

public interface TimeRange {
  
  LocalDateTime getStartTime();
  LocalDateTime getEndTime();

}
