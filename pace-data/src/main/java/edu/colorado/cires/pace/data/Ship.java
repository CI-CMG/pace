package edu.colorado.cires.pace.data;

import java.util.UUID;

public record Ship(UUID uuid, String name) implements ObjectWithName {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Ship(
        uuid,
        name
    );
  }
}
