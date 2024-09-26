package edu.colorado.cires.pace.data.object.instrument;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Instrument extends ObjectWithName and holds fileTypes
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class Instrument extends ObjectWithName {
  
  @NotEmpty @NotNull
  private final List<@NotBlank String> fileTypes;

  /**
   * Returns a new object with the provided uuid set
   *
   * @param uuid field for assigning uuid to new object
   * @return AbstractObject with provided uuid
   */
  @Override
  public AbstractObject setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a new instrument with the provided visibility set
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return Instrument with provided visibility
   */
  @Override
  public Instrument setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
