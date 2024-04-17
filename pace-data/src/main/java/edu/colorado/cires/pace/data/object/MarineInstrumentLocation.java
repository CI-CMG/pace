package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidMarineInstrumentLocation;
import jakarta.validation.constraints.Negative;
import jakarta.validation.constraints.NegativeOrZero;

@ValidMarineInstrumentLocation
public interface MarineInstrumentLocation extends LatLonPair {
  @Negative
  Float getSeaFloorDepth();
  @NegativeOrZero
  Float getInstrumentDepth();
}
