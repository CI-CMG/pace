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

  @Builder.Default
  private final String sensor = "";
  private final String channelStart;
  private final String channelEnd;
  private final PassivePackerSamplingDetails samplingDetails;

}
