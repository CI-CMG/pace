package edu.colorado.cires.pace.data;

import java.util.UUID;

public record Sea(UUID uuid, String name) implements ObjectWithName {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Sea(
        uuid,
        name
    );
  }
}
