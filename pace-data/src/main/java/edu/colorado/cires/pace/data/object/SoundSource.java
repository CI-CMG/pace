package edu.colorado.cires.pace.data.object;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(toBuilder = true)
@Jacksonized
public class SoundSource implements ObjectWithName {
  
  private final UUID uuid;
  private final String name;
  private final String scientificName;

}
