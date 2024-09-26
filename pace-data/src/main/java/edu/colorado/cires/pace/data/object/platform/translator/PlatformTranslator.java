package edu.colorado.cires.pace.data.object.platform.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * PlatformTranslator extends Translator and holds onto platformUUID and platformName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class PlatformTranslator extends Translator {
  private final String platformUUID;
  private final String platformName;

  /**
   * Returns new object with specified uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with specified uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns translator with specified visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return PlatformTranslator with specified visibility
   */
  @Override
  public PlatformTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
