package edu.colorado.cires.pace.data.object.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

public interface AbstractObject {

  @JsonIgnore
  String getUniqueField();

  AbstractObject setUuid(UUID uuid);

  AbstractObject setVisible(boolean visible);

  UUID getUuid();

  boolean isVisible();
}
