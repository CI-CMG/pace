package edu.colorado.cires.pace.data;

import java.util.UUID;

public record Platform(UUID uuid, String name) implements ObjectWithName {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Platform(
        uuid,
        name
    );
  }
}
