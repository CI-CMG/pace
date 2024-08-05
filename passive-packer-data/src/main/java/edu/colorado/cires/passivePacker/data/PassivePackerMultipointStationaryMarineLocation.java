package edu.colorado.cires.passivePacker.data;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PassivePackerMultipointStationaryMarineLocation extends PassivePackerLocation {
  
  @Builder.Default
  private final String deployShip = "";
  private final String seaArea;
  private final List<PassivePackerMarineInstrumentLocation> locations;

}
