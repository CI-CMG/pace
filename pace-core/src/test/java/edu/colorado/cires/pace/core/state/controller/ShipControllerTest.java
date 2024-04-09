package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Ship;
import java.util.UUID;
import java.util.function.Supplier;

class ShipControllerTest extends CRUDControllerTest<Ship> {

  @Override
  protected CRUDController<Ship> createController(Datastore<Ship> datastore) {
    return new ShipController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Ship createNewObject(boolean withUUID) {
    return new Ship(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected Ship setUniqueField(Ship object, String uniqueField) {
    return new Ship(
        object.uuid(),
        uniqueField
    );
  }
}
