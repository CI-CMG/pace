package edu.colorado.cires.pace.data.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class ObjectWithUniqueField {
  
  private final UUID uuid;
  
  @JsonIgnore
  public abstract String getUniqueField();
  
  public abstract ObjectWithUniqueField setUuid(UUID uuid);

}
