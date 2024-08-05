package edu.colorado.cires.passivePacker.data;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder
@Jacksonized
public class PassivePackerChannel {
  
  private final String channelStart;
  private final String channelEnd;
  private final String sensor;
  private final PassivePackerSamplingDetails samplingDetails;

}
