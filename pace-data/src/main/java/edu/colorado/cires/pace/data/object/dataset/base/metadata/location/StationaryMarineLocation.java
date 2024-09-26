package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * StationaryMarineLocation implements MarineLocation and adds in
 * seaArea, deployment location, and recovery location variables
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class StationaryMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotNull @Valid
  private final MarineInstrumentLocation deploymentLocation;
  @NotNull @Valid
  private final MarineInstrumentLocation recoveryLocation;

  /**
   * Returns a new object with the provided seaArea set
   *
   * @param seaArea relevant sea area to apply to new object
   * @return MarineLocation with provided seaArea set
   */
  @Override
  public MarineLocation setSeaArea(String seaArea) {
    return toBuilder()
        .seaArea(seaArea)
        .build();
  }
}
