package edu.colorado.cires.passivePacker.data;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder
@Jacksonized
public class PassivePackerMarineInstrumentLocation {
  
  private final String latitude;
  private final String longitude;
  private final String bottomDepth;
  private final String instrumentDepth;

}
