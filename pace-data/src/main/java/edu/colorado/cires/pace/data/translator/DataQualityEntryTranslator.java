package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DataQualityEntryTranslator {
  
  private final TimeTranslator startTimeTranslator;
  private final TimeTranslator endTimeTranslator;
  private final String minFrequency;
  private final String maxFrequency;
  private final String qualityLevel;
  private final String comments;

}
