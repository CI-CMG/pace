package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import edu.colorado.cires.pace.data.validation.ValidMarineInstrumentLocation;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * MarineInstrumentLocation holds onto both a lat/lon pair and
 * a depth pair while using the implemented LatlonPair functions
 */
@ValidMarineInstrumentLocation
@Builder(toBuilder = true)
@Jacksonized
@Data
public class MarineInstrumentLocation implements LatLonPair {
  
  private final Double latitude;
  private final Double longitude;
  private Float seaFloorDepth;
  private Float instrumentDepth;
}
