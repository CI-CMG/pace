package edu.colorado.cires.pace.data.object.dataset.passivePacker;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode
@Builder
@Jacksonized
public class PassivePackerChannel {
  
  private final LocalDateTime channelStart;
  private final LocalDateTime channelEnd;
  private final String sensor;
  private final PassivePackerSamplingDetails samplingDetails;

}
