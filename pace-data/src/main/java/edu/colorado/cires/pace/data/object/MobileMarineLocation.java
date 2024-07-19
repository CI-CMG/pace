package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class MobileMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotBlank
  private final String vessel;
  @NotBlank
  private final String locationDerivationDescription;

  @Override
  public MobileMarineLocation setSeaArea(String seaArea) {
    return toBuilder()
        .seaArea(seaArea)
        .build();
  }
  
  public MobileMarineLocation setVessel(String vessel) {
    return toBuilder().vessel(vessel).build();
  }
}
