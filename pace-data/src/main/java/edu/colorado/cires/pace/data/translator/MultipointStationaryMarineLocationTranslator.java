package edu.colorado.cires.pace.data.translator;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class MultipointStationaryMarineLocationTranslator implements LocationDetailTranslator {
  
  private final String seaArea;
  private final List<MarineInstrumentLocationTranslator> locationTranslators;

}
