package edu.colorado.cires.pace.data.object;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class Channel implements TimeRange {
  
  @NotNull @Valid
  private final PackageSensor sensor;
  private final LocalDateTime startTime;
  private final LocalDateTime endTime;
  private final List<SampleRate> sampleRates;
  private final List<DutyCycle> dutyCycles;
  private final List<Gain> gains;
  
}
