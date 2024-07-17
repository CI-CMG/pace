package edu.colorado.cires.pace.data.object;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionType extends ObjectWithUniqueField {
  @NotBlank
  private final String source;
  private final String scienceName;

  @Override
  public String getUniqueField() {
    return source;
  }
}
