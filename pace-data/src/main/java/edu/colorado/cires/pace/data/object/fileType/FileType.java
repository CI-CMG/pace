package edu.colorado.cires.pace.data.object.fileType;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * FileType extends ObjectWithUniqueField and holds type and comment fields
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class FileType extends ObjectWithUniqueField {

  @NotBlank
  final String type;
  final String comment;

  /**
   * Returns the unique field (type)
   *
   * @return String unique field
   */
  @Override
  public String getUniqueField() {
    return type;
  }

  /**
   * Returns a new object with the specified uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with the provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new object with the specified visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return FileType with the provided visibility
   */
  @Override
  public FileType setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
