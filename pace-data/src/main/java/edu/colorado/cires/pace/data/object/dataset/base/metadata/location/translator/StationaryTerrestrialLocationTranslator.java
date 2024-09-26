package edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * StationaryTerrestrialLocationTranslator holds the necessary headers for translators of this type
 * as an implementation of the LocationDetailTranslator type
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class StationaryTerrestrialLocationTranslator implements LocationDetailTranslator {
  
  private final String latitude;
  private final String longitude;
  private final String surfaceElevation;
  private final String instrumentElevation;

}
