package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class MultiPointStationaryMarineLocation implements MarineLocation {
  
  private final Sea seaArea;
  @NotNull @NotEmpty
  private final List<@Valid MarineInstrumentLocation> locations;
  
}
