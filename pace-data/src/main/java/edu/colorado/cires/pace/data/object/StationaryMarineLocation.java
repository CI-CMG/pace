package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class StationaryMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotNull @Valid
  private final MarineInstrumentLocation deploymentLocation;
  @NotNull @Valid
  private final MarineInstrumentLocation recoveryLocation;
  
}
