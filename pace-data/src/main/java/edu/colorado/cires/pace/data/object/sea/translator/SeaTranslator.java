package edu.colorado.cires.pace.data.object.sea.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * SeaTranslator extends Translator and holds seaUUID and seaName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class SeaTranslator extends Translator {
  private final String seaUUID;
  private final String seaName;

  /**
   * Returns object with specified uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with specified uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns translator object with provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return SeaTranslator with provided visibility
   */
  @Override
  public SeaTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
