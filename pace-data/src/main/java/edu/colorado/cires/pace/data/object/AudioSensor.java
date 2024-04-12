package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.AudioSensorValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class AudioSensor implements Sensor {
  
  private UUID uuid;
  private String name;
  private Position position;
  private String description;
  private String hydrophoneId;
  private String preampId;

  @Builder(toBuilder = true)
  @Jacksonized
  public AudioSensor(UUID uuid, String name, Position position, String description, String hydrophoneId, String preampId) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.position = position;
    this.description = description;
    this.hydrophoneId = hydrophoneId;
    this.preampId = preampId;
    
    new AudioSensorValidator().validate(this);
  }
}
