package edu.colorado.cires.pace.data.object.fileType.translator;

import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Jacksonized
public class FileTypeTranslator extends Translator {
  private final String fileTypeUUID;
  private final String type;
  private final String comment;

  @Override
  public ObjectWithUniqueField setUuid(UUID uuid) {
    return toBuilder().uuid(uuid).build();
  }

  @Override
  public FileTypeTranslator setVisible(boolean visible) {
    return toBuilder().visible(visible).build();
  }
}
