package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class AudioSensor implements Sensor {
  
  private UUID uuid;
  private String name;
  private Position position;
  private String description;
  @NotBlank
  private String hydrophoneId;
  @NotBlank
  private String preampId;
  
}
