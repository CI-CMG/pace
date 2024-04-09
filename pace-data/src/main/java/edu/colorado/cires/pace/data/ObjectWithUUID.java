package edu.colorado.cires.pace.data;

import java.util.UUID;

public interface ObjectWithUUID {
  
  UUID uuid();
  
  ObjectWithUUID copyWithNewUUID(UUID uuid);

}
