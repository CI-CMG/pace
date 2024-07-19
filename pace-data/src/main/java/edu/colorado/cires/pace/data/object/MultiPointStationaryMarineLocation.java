package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class MultiPointStationaryMarineLocation implements MarineLocation {
  
  private final String seaArea;
  @NotNull @NotEmpty
  private final List<@Valid MarineInstrumentLocation> locations;

  @Override
  public MultiPointStationaryMarineLocation setSeaArea(String seaArea) {
    return toBuilder()
        .seaArea(seaArea)
        .build();
  }
}
