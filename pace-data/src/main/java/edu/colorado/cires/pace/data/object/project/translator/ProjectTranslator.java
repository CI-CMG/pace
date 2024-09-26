package edu.colorado.cires.pace.data.object.project.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * ProjectTranslator extends Translator and holds projectUUID and projectName
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class ProjectTranslator extends Translator {
  private final String projectUUID;
  private final String projectName;

  /**
   * Returns new translator with provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return ProjectTranslator with provided uuid
   */
  @Override
  public ProjectTranslator setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns object with provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return AbstractObject with provided visibility
   */
  @Override
  public AbstractObject setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
