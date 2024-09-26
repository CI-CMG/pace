package edu.colorado.cires.pace.data.object.ship;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Ship extends ObjectWithName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Ship extends ObjectWithName {

  /**
   * Returns new object with the provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with the provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns new ship object with the provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return Ship with provided visibility
   */
  @Override
  public Ship setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
