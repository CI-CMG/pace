package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.DepthSensorValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class DepthSensor implements Sensor {
  private final UUID uuid;
  private final String name;
  private final Position position;
  private final String description;

  @Builder(toBuilder = true)
  @Jacksonized
  public DepthSensor(UUID uuid, String name, Position position, String description) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.position = position;
    this.description = description;
    
    new DepthSensorValidator().validate(this);
  }
}
