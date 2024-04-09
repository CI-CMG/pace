package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Platform;
import java.util.UUID;
import java.util.function.Supplier;

class PlatformControllerTest extends CRUDControllerTest<Platform> {

  @Override
  protected CRUDController<Platform> createController(Datastore<Platform> datastore) {
    return new PlatformController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Platform createNewObject(boolean withUUID) {
    return new Platform(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected Platform setUniqueField(Platform object, String uniqueField) {
    return new Platform(
        object.uuid(),
        uniqueField
    );
  }
}
