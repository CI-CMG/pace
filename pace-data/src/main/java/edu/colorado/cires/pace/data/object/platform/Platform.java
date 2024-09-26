package edu.colorado.cires.pace.data.object.platform;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Platform extends ObjectWithName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Platform extends ObjectWithName {

  /**
   * Returns a new object with the provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with provided uuid set
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns platform object with the provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return Platform with provided visibility
   */
  @Override
  public Platform setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
