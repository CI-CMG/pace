package edu.colorado.cires.pace.data.object.fileType.translator;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * FileTypeTranslator extends Translator and holds onto fileTypeUUID, type, and comment
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class FileTypeTranslator extends Translator {
  private final String fileTypeUUID;
  private final String type;
  private final String comment;

  /**
   * Returns an object with the specified uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return Abstract object with provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a translator with the specified visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return FileTypeTranslator with specified visibility
   */
  @Override
  public FileTypeTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
