package edu.colorado.cires.pace.data.object.detectionType;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * DetectionType extends ObjectWithUniqueField and holds onto source and science name
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class DetectionType extends ObjectWithUniqueField {
  @NotBlank
  private final String source;
  private final String scienceName;

  /**
   * Returns the unique field
   *
   * @return String unique field
   */
  @Override
  public String getUniqueField() {
    return source;
  }

  /**
   * Returns a new object with the provided uuid set
   *
   * @param uuid field for assigning uuid to new object
   * @return Object with specified uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new detection type with the provided visibility set
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return DetectionType with specified visibility
   */
  @Override
  public DetectionType setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
