package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.translator.TimeTranslator;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * GainTranslator holds header names for fields of gain within a channel
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class GainTranslator {

  private final TimeTranslator startTime;
  private final TimeTranslator endTime;
  private final String gain;

}
