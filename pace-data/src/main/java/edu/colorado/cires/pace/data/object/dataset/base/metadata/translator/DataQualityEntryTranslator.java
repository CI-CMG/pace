package edu.colorado.cires.pace.data.object.dataset.base.metadata.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * DataQualityEntryTranslator holds onto the necessary headers for times,
 * frequencies, quality level, comments, and channel numbers for translation
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class DataQualityEntryTranslator {
  
  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final String minFrequency;
  private final String maxFrequency;
  private final String qualityLevel;
  private final String comments;
  private final String channelNumbers;

}
