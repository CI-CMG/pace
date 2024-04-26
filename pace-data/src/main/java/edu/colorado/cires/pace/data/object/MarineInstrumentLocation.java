package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidMarineInstrumentLocation;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@ValidMarineInstrumentLocation
@Builder(toBuilder = true)
@Jacksonized
@Data
public class MarineInstrumentLocation implements LatLonPair {
  
  private final Float latitude;
  private final Float longitude;
  private Float seaFloorDepth;
  private Float instrumentDepth;
}
