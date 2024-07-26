package edu.colorado.cires.pace.data.object.detectionType.translator;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionTypeTranslator extends Translator {
  private final String detectionTypeUUID;
  private final String source;
  private final String scienceName;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public DetectionTypeTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
