package edu.colorado.cires.pace.data;

import java.util.List;
import java.util.UUID;

public record Instrument(UUID uuid, String name, List<FileType> fileTypes) implements ObjectWithName {

  @Override
  public ObjectWithUUID copyWithNewUUID(UUID uuid) {
    return new Instrument(
        uuid,
        name,
        fileTypes
    );
  }
}
