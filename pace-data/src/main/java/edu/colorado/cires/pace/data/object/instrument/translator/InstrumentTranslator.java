package edu.colorado.cires.pace.data.object.instrument.translator;

import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * InstrumentTranslator extends Translator and holds onto instrumentUUID,
 * instrumentName, and fileTypes
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class InstrumentTranslator extends Translator {
  private final String instrumentUUID;
  private final String instrumentName;
  private final String fileTypes;

  /**
   * Returns a translator with the provided uuid
   *
   * @param uuid field for assigning uuid to new object
   * @return InstrumentTranslator with the provided uuid
   */
  @Override
  public InstrumentTranslator setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  /**
   * Returns a translator with the provided visibility
   *
   * @param visible boolean which indicates whether to make the object visible
   *                or invisible
   * @return InstrumentTranslator with the provided visibility
   */
  @Override
  public InstrumentTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
