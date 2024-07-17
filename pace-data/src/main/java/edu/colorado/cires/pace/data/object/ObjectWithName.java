package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public abstract class ObjectWithName extends ObjectWithUniqueField {
  @NotBlank
  private final String name;

  @Override
  public String getUniqueField() {
    return name;
  }
}
