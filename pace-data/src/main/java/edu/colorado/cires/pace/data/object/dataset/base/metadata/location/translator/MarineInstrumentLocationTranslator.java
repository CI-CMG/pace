package edu.colorado.cires.pace.data.object.dataset.base.metadata.location.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class MarineInstrumentLocationTranslator {
  
  private final String latitude;
  private final String longitude;
  private final String seaFloorDepth;
  private final String instrumentDepth;

}
