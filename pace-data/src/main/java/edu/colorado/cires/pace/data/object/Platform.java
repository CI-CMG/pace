package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.PlatformValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class Platform implements ObjectWithName {

  final UUID uuid;
  final String name;

  @Builder(toBuilder = true)
  @Jacksonized
  private Platform(UUID uuid, String name) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    
    new PlatformValidator().validate(this);
  }
}
