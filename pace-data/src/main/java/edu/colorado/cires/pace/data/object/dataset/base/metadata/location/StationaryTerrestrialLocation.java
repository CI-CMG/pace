package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import java.util.OptionalDouble;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * StationaryTerrestrialLocation implements LocationDetail, LatLonPair, and ElevationPair,
 * providing latitude, longitude, surface elevation, and instrument elevation variables
 * to assign
 */
@Data
@Builder
@Jacksonized
public class StationaryTerrestrialLocation implements LocationDetail, LatLonPair, ElevationPair {
  
  private final Double latitude;
  private final Double longitude;
  private final Float surfaceElevation;
  private final Float instrumentElevation;

  @Override
  public OptionalDouble resolveLatitude() {
    if (latitude == null) {
      return OptionalDouble.empty();
    }
    return OptionalDouble.of(latitude);
  }

  @Override
  public OptionalDouble resolveLongitude() {
    if (longitude == null) {
      return OptionalDouble.empty();
    }
    return OptionalDouble.of(longitude);
  }
}

