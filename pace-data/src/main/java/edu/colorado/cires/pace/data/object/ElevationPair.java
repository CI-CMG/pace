package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ValidElevationPair;
import jakarta.validation.constraints.PositiveOrZero;

@ValidElevationPair
public interface ElevationPair {

  @PositiveOrZero
  Float getSurfaceElevation();
  @PositiveOrZero
  Float getInstrumentElevation();

}
