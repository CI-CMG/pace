package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface MarineLocation extends LocationDetail {
  @NotNull @Valid
  Sea getSeaArea();

}
