package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Ship;

public class ShipRepository extends CRUDRepository<Ship> {

  public ShipRepository(Datastore<Ship> datastore) {
    super(datastore);
  }

  @Override
  protected String getObjectName() {
    return Ship.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
