package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * MultiPointStationaryMarineLocation implements MarineLocation and adds in
 * seaArea and locations fields
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class MultiPointStationaryMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotNull @NotEmpty @Builder.Default
  private final List<@Valid MarineInstrumentLocation> locations = Collections.emptyList();

  /**
   * Returns a new object with the provided seaArea set
   *
   * @param seaArea relevant sea area to apply to new object
   * @return MultiPointStationaryMarineLocation with provided seaArea set
   */
  @Override
  public MultiPointStationaryMarineLocation setSeaArea(String seaArea) {
    return toBuilder()
        .seaArea(seaArea)
        .build();
  }
}
