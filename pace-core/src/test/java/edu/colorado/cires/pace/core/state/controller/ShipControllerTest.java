package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Ship;
import java.util.UUID;
import java.util.function.Supplier;

class ShipControllerTest extends CRUDControllerTest<Ship, String> {

  @Override
  protected CRUDController<Ship, String> createController(Datastore<Ship, String> datastore) {
    return new ShipController(datastore);
  }

  @Override
  protected UniqueFieldProvider<Ship, String> getUniqueFieldProvider() {
    return Ship::getName;
  }

  @Override
  protected UUIDProvider<Ship> getUuidProvider() {
    return Ship::getUUID;
  }

  @Override
  protected UniqueFieldSetter<Ship, String> getUniqueFieldSetter() {
    return Ship::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Ship createNewObject(boolean withUUID) {
    Ship ship = new Ship();
    if (withUUID) {
      ship.setUUID(UUID.randomUUID());
    }
    ship.setUse(true);
    ship.setName(UUID.randomUUID().toString());
    return ship;
  }
}
