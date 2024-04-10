package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.Platform;
import java.util.UUID;

public class PlatformRepository extends CRUDRepository<Platform> {

  public PlatformRepository(Datastore<Platform> datastore) {
    super(datastore);
  }

  @Override
  protected Platform setUUID(Platform object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
