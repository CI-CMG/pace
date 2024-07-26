package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import java.time.LocalDateTime;

public interface AudioTimeRange {
  
  LocalDateTime getAudioStartTime();
  LocalDateTime getAudioEndTime();
  
}
