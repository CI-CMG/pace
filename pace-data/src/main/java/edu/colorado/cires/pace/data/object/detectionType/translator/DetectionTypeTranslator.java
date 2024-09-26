package edu.colorado.cires.pace.data.object.detectionType.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * DetectionTypeTranslator extends Translator and holds source, scienceName,
 * and detectionTypeUUID fields
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionTypeTranslator extends Translator {
  private final String detectionTypeUUID;
  private final String source;
  private final String scienceName;

  /**
   * Returns and object with the uuid set as provided
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with the provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns and object with the visibility set as provided
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return AbstractObject with the provided visibility
   */
  @Override
  public DetectionTypeTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
