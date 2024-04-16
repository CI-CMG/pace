package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.validation.SoundSourceValidator;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
public class SoundSource implements ObjectWithName {
  
  private final UUID uuid;
  private final String name;
  private final String scientificName;

  @Builder
  @Jacksonized
  private SoundSource(UUID uuid, String name, String scientificName) throws ValidationException {
    this.uuid = uuid;
    this.name = name;
    this.scientificName = scientificName;
    
    new SoundSourceValidator().validate(this);
  }
}
