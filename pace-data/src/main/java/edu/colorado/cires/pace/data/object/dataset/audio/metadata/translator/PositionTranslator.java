package edu.colorado.cires.pace.data.object.dataset.audio.metadata.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * PositionTranslator holds headers for x, y, and z fields
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class PositionTranslator {
  
  private final String x;
  private final String y;
  private final String z;

}
