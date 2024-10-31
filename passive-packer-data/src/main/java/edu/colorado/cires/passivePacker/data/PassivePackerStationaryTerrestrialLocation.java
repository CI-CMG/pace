package edu.colorado.cires.passivePacker.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * PassivePackerStationaryTerrestrialLocation extends PassivePackerLocation and provides builder structure for
 * PassivePackerStationaryTerrestrialLocation objects
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerStationaryTerrestrialLocation extends PassivePackerLocation {
  
  private final String lat;
  private final String lon;
  private final String instrumentElevation;
  private final String surfaceElevation;

}
