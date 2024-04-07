package edu.colorado.cires.pace.core.state.repository;

import java.util.UUID;

@FunctionalInterface
public interface UUIDSetter<O> {

  void setUUID(O object, UUID uuid);
  
}
