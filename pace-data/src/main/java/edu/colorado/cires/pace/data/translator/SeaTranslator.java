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
public class SeaTranslator extends Translator {
  private final String seaUUID;
  private final String seaName;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }
}
