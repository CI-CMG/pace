package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.OptionalDouble;
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

  @Override
  public OptionalDouble resolveLatitude() {
    if (getDeploymentLocation().getLatitude() == null) {
      return OptionalDouble.empty();
    }
    return OptionalDouble.of(getDeploymentLocation().getLatitude());
  }

  @Override
  public OptionalDouble resolveLongitude() {
    if (getDeploymentLocation().getLongitude() == null) {
      return OptionalDouble.empty();
    }
    return OptionalDouble.of(getDeploymentLocation().getLongitude());
  }
}
