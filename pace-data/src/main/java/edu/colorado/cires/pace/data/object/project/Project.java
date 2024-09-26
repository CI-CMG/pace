package edu.colorado.cires.pace.data.object.project;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Project extends ObjectWithName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Project extends ObjectWithName {

  /**
   * Returns object with provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns project object with provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return Project with specified visibility
   */
  @Override
  public Project setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
