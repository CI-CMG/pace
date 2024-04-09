package edu.colorado.cires.pace.data;

import java.util.UUID;

public record Project(UUID uuid, String name) implements ObjectWithName {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Project(
        uuid,
        name
    );
  }
}
