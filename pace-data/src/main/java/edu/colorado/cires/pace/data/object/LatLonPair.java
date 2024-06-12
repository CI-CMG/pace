package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public interface LatLonPair {

  @NotNull @Min(-90) @Max(90)
  Double getLatitude();
  @NotNull @Min(-180) @Max(180)
  Double getLongitude();

}
