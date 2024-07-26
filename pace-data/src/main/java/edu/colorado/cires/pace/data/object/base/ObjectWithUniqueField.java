package edu.colorado.cires.pace.data.object.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class ObjectWithUniqueField {
  
  private final UUID uuid;
  @NotNull @Builder.Default
  private final boolean visible = true;
  
  @JsonIgnore
  public abstract String getUniqueField();
  
  public abstract ObjectWithUniqueField setUuid(UUID uuid);
  
  public abstract ObjectWithUniqueField setVisible(boolean visible);

}
