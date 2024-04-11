package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Platform;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class PlatformRepository extends CRUDRepository<Platform> {

  public PlatformRepository(Datastore<Platform> datastore) {
    super(datastore);
  }

  @Override
  protected Platform setUUID(Platform object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
