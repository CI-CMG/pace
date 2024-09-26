package edu.colorado.cires.pace.data.object.ship.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * ShipTranslator extends Translator and holds shipUUID and shipName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class ShipTranslator extends Translator {
  private final String shipUUID;
  private final String shipName;

  /**
   * Returns new object with provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with the provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns new translator with the provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return ShipTranslator with the provided visibility
   */
  @Override
  public ShipTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
