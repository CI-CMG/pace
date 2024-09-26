package edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator;

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * MultipointStationaryMarineLocationTranslator holds the necessary headers for translators of this type
 * as an implementation of the LocationDetailTranslator type
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class MultipointStationaryMarineLocationTranslator implements LocationDetailTranslator {
  
  private final String seaArea;
  @Builder.Default
  private final List<MarineInstrumentLocationTranslator> locationTranslators = Collections.emptyList();

}
