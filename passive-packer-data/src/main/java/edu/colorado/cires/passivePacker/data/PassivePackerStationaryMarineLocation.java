package edu.colorado.cires.passivePacker.data;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerStationaryMarineLocation extends PassivePackerLocation {
  private final String seaArea;
  private final String deployLat;
  private final String deployLon;
  @Builder.Default
  private final String deployShip = "";
  private final String deployInstrumentDepth;
  private final String deployBottomDepth;
  private final String recoverLat;
  private final String recoverLon;
  @Builder.Default
  private final String recoverShip = "";
  private final String recoverInstrumentDepth;
  private final String recoverBottomDepth;
}
