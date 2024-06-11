package edu.colorado.cires.pace.data.translator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class StationaryTerrestrialLocationTranslator implements LocationDetailTranslator {
  
  private final String latitude;
  private final String longitude;
  private final String surfaceElevation;
  private final String instrumentElevation;

}
