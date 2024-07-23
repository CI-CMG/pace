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
public class ProjectTranslator extends Translator {
  private final String projectUUID;
  private final String projectName;

  @Override
  public ProjectTranslator setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public ObjectWithUniqueField setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
