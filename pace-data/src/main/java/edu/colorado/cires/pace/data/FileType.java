package edu.colorado.cires.pace.data;

import java.util.UUID;

public record FileType(UUID uuid, String type, String comment) implements ObjectWithUniqueField {

  @Override
  public String uniqueField() {
    return type;
  }

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new FileType(
        uuid,
        type,
        comment
    );
  }
}
