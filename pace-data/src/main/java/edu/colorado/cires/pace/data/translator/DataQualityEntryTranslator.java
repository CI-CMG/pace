package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DataQualityEntryTranslator {
  
  private final String startTime;
  private final String endTime;
  private final String minFrequency;
  private final String maxFrequency;
  private final String qualityLevel;
  private final String comments;

}
