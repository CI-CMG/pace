package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.Ship;
import java.util.UUID;

public class ShipRepository extends CRUDRepository<Ship> {

  public ShipRepository(Datastore<Ship> datastore) {
    super(datastore);
  }

  @Override
  protected Ship setUUID(Ship object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
