package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class InstrumentTranslator extends Translator {
  private final String instrumentUUID;
  private final String instrumentName;
  private final String fileTypes;

  @Override
  public InstrumentTranslator setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public InstrumentTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
