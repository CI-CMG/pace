package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Provides the pair of lat/lon getters for latitude and longitude elevation
 */
public interface LatLonPair {

  /**
   * Returns the latitude
   * @return Double of latitude
   */
  @NotNull @Min(-90) @Max(90)
  Double getLatitude();

  /**
   * Returns the longitude
   * @return Double of the longitude
   */
  @NotNull @Min(-180) @Max(180)
  Double getLongitude();

}
