package edu.colorado.cires.pace.data;

import java.util.UUID;

public record DetectionType(UUID uuid, String source, String scienceName) implements ObjectWithUniqueField {

  @Override
  public String uniqueField() {
    return scienceName;
  }

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new DetectionType(
        uuid,
        source,
        scienceName
    );
  }
}
