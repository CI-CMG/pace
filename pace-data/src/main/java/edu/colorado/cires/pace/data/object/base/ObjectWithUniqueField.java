package edu.colorado.cires.pace.data.object.base;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * ObjectWithUniqueField holds uuid and visible fields as well as all
 * fields from AbstractObject
 */
@Data
@SuperBuilder(toBuilder = true)
public abstract class ObjectWithUniqueField implements AbstractObject {
  
  private final UUID uuid;
  @NotNull @Builder.Default
  private final boolean visible = true;

}
