package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.OtherSensorValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class OtherSensor implements Sensor {
  
  private UUID uuid;
  private String name;
  private Position position;
  private String description;
  private String sensorType;
  private String properties;

  @Builder(toBuilder = true)
  @Jacksonized
  public OtherSensor(UUID uuid, String name, Position position, String description, String sensorType, String properties) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.position = position;
    this.description = description;
    this.sensorType = sensorType;
    this.properties = properties;
    
    new OtherSensorValidator().validate(this);
  }
}
