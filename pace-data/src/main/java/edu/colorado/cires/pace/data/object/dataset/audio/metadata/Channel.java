package edu.colorado.cires.pace.data.object.dataset.audio.metadata;

import edu.colorado.cires.pace.data.object.dataset.base.metadata.PackageSensor;
import edu.colorado.cires.pace.data.object.dataset.base.metadata.TimeRange;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Channel<T> implements TimeRange {
  
  @NotNull @Valid
  private final PackageSensor<T> sensor;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final List<SampleRate> sampleRates;
  private final List<DutyCycle> dutyCycles;
  private final List<Gain> gains;
  
}
