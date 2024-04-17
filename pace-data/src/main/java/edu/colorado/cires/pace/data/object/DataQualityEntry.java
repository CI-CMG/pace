package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class DataQualityEntry implements TimeRange, FrequencyRange {
  
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final Float minFrequency;
  private final Float maxFrequency;
  private final QualityLevel qualityLevel;
  private final String comments;
  
}
