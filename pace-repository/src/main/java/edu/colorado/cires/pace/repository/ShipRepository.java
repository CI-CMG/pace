package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.datastore.Datastore;
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
