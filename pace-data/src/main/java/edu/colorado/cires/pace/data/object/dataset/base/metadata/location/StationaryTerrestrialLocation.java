package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class StationaryTerrestrialLocation implements LocationDetail, LatLonPair, ElevationPair {
  
  private final Double latitude;
  private final Double longitude;
  private final Float surfaceElevation;
  private final Float instrumentElevation;
  
}
