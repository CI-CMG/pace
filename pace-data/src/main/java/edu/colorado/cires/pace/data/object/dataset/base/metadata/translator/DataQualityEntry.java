package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.FrequencyRange;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.QualityLevel;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
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
  @Builder.Default
  private final List<Integer> channelNumbers = Collections.emptyList();
  
}
