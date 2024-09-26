package edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * StationaryMarineLocationTranslator holds the necessary headers for translators of this type
 * as an implementation of the LocationDetailTranslator type
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class StationaryMarineLocationTranslator implements LocationDetailTranslator {
  
  private final String seaArea;
  private final MarineInstrumentLocationTranslator deploymentLocationTranslator;
  private final MarineInstrumentLocationTranslator recoveryLocationTranslator;

}
