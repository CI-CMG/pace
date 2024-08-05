package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

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
