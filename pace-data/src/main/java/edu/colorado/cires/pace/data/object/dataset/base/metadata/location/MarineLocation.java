package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.constraints.NotBlank;

public interface MarineLocation extends LocationDetail {
  @NotBlank
  String getSeaArea();
  
  MarineLocation setSeaArea(String seaArea);

}
