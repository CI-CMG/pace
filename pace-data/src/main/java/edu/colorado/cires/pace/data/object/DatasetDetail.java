package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DatasetDetail extends TimeRange {
  
  @NotNull @Valid
  Platform getPlatform();
  @NotNull @Valid
  Instrument getInstrument();

}
