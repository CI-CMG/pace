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
public class PassivePackerMobileMarineLocation extends PassivePackerLocation {
  
  private final String seaArea;
  private final String deployShip;
  @Builder.Default
  private final String files = "";
  private final String positionDetails;

}
