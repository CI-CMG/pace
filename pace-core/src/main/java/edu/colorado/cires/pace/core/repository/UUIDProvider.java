package edu.colorado.cires.pace.core.repository;

import java.util.UUID;

@FunctionalInterface
public interface UUIDProvider<O> {
  
  UUID getUUID(O object);

}
