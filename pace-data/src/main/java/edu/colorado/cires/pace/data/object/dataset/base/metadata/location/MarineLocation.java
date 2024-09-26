package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.constraints.NotBlank;

/**
 * MarineLocation extends LocationDetail while adding in a seaArea field's
 * setter and getter
 */
public interface MarineLocation extends LocationDetail {
  @NotBlank
  String getSeaArea();
  
  MarineLocation setSeaArea(String seaArea);

}
