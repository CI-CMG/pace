package edu.colorado.cires.pace.data.object.detectionType;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
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

  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetectionType setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
