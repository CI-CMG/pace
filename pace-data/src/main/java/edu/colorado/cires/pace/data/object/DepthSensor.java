package edu.colorado.cires.pace.data.object;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class DepthSensor implements Sensor {
  
  private final UUID uuid;
  private final String name;
  private final Position position;
  private final String description;
  
}
