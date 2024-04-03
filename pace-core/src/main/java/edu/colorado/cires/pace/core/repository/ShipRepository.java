package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.data.Ship;

public class ShipRepository extends CRUDRepository<Ship, String> {

  protected ShipRepository(Datastore<Ship, String> datastore) {
    super(Ship::getUUID, Ship::getName, Ship::setUUID, datastore);
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
