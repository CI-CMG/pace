package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class AudioSensor extends Sensor {
  @NotBlank
  private String hydrophoneId;
  @NotBlank
  private String preampId;
}
