package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.ShipValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Ship implements ObjectWithName {
  
  private final UUID uuid;
  private final String name;

  @Builder(toBuilder = true)
  @Jacksonized
  private Ship(UUID uuid, String name) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    
    new ShipValidator().validate(this);
  }
}
