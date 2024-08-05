package edu.colorado.cires.pace.data.object.dataset.passivePacker;

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
  private final String files;
  private final String positionDetails;

}
