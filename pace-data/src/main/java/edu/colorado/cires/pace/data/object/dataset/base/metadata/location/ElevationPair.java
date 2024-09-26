package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import edu.colorado.cires.pace.data.validation.ValidElevationPair;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Provides the pair of elevation getters for surface and instrument elevation
 */
@ValidElevationPair
public interface ElevationPair {

  /**
   * Returns the surface elevation of the object
   * @return Float surface elevation
   */
  @PositiveOrZero
  Float getSurfaceElevation();

  /**
   * Returns the instrument elevation of the object
   * @return Float instrument elevation
   */
  @PositiveOrZero
  Float getInstrumentElevation();

}
