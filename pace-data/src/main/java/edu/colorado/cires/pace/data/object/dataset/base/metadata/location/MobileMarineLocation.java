package edu.colorado.cires.pace.data.object.dataset.base.metadata.location;

import jakarta.validation.constraints.NotBlank;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

/**
 * MobileMarineLocation implements MarineLocation, holding onto
 * relevant fields such as seaArea, vessel, locationDerivationDescription,
 * and fileList (which holds the paths to the files associated)
 */
@Data
@Builder(toBuilder = true)
@Jacksonized
public class MobileMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotBlank
  private final String vessel;
  @NotBlank
  private final String locationDerivationDescription;
  @Builder.Default
  private final List<Path> fileList = Collections.emptyList();

  /**
   * Returns a new object with the provided seaArea declared
   *
   * @param seaArea relevant sea area to apply to new object
   * @return MobileMarineLocation object with supplied seaArea
   */
  @Override
  public MobileMarineLocation setSeaArea(String seaArea) {
    return toBuilder()
        .seaArea(seaArea)
        .build();
  }

  /**
   * Returns a new object with the provided vessel declared
   *
   * @param vessel relevant vessel to apply to new object
   * @return MobileMarineLocation object with supplied vessel
   */
  public MobileMarineLocation setVessel(String vessel) {
    return toBuilder().vessel(vessel).build();
  }
}
