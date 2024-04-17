package edu.colorado.cires.pace.data.object;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class StationaryTerrestrialLocation implements LocationDetail, LatLonPair, ElevationPair {
  
  private final Float latitude;
  private final Float longitude;
  private final Float surfaceElevation;
  private final Float instrumentElevation;
  
}
