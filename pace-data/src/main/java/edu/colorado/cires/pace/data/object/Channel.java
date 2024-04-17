package edu.colorado.cires.pace.data.object;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Channel implements TimeRange {
  
  private final Sensor sensor;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final List<SampleRate> sampleRates;
  private final List<DutyCycle> dutyCycles;
  private final List<Gain> gains;
  
}
