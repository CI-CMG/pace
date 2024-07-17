package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;

public interface MarineLocation extends LocationDetail {
  @NotBlank
  String getSeaArea();

}
