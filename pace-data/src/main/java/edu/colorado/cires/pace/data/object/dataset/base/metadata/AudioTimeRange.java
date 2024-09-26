package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import java.time.LocalDateTime;

/**
 * AudioTimeRange provides getters for audio start and end time
 */
public interface AudioTimeRange {

  /**
   * Returns audio start time
   * @return LocalDateTime audio start time
   */
  LocalDateTime getAudioStartTime();

  /**
   * Returns audio end time
   * @return LocalDateTime audio end time
   */
  LocalDateTime getAudioEndTime();
  
}
