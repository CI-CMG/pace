package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class StationaryMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotNull @Valid
  private final MarineInstrumentLocation deploymentLocation;
  @NotNull @Valid
  private final MarineInstrumentLocation recoveryLocation;

  @Override
  public MarineLocation setSeaArea(String seaArea) {
    return toBuilder()
        .seaArea(seaArea)
        .build();
  }
}
