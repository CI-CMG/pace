package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;

public interface AudioTimeRange {
  
  LocalDateTime getAudioStartTime();
  LocalDateTime getAudioEndTime();
  
}
