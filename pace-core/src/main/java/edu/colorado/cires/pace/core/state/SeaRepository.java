package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.Sea;
import java.util.UUID;

public class SeaRepository extends CRUDRepository<Sea> {

  public SeaRepository(Datastore<Sea> datastore) {
    super(datastore);
  }

  @Override
  protected Sea setUUID(Sea object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
