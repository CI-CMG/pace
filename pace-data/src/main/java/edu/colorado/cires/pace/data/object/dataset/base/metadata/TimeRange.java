package edu.colorado.cires.pace.data.object.dataset.base.metadata;

import edu.colorado.cires.pace.data.validation.ValidTimeRange;
import java.time.LocalDateTime;

/**
 * TimeRange provides getters for start and end time
 */
@ValidTimeRange
public interface TimeRange {

  /**
   * Returns start time
   * @return LocalDateTime start time
   */
  LocalDateTime getStartTime();

  /**
   * Returns end time
   * @return LocalDateTime end time
   */
  LocalDateTime getEndTime();
}
