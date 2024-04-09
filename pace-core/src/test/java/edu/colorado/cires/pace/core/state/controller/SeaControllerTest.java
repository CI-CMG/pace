package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;
import java.util.function.Supplier;

class SeaControllerTest extends CRUDControllerTest<Sea> {

  @Override
  protected CRUDController<Sea> createController(Datastore<Sea> datastore) {
    return new SeaController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Sea createNewObject(boolean withUUID) {
    return new Sea(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected Sea setUniqueField(Sea object, String uniqueField) {
    return new Sea(
        object.uuid(),
        uniqueField
    );
  }
}
