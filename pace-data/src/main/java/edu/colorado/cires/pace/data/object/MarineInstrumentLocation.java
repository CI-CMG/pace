package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidMarineInstrumentLocation;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

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
