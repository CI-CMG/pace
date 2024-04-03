package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.Ship;

public abstract class ShipRepository extends CRUDRepository<Ship, String> {

  protected ShipRepository() {
    super(Ship::getUUID, Ship::getName, Ship::setUUID);
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
