package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.UUID;
import java.util.function.Supplier;

class DetectionTypeControllerTest extends CRUDControllerTest<DetectionType> {

  @Override
  protected CRUDController<DetectionType> createController(Datastore<DetectionType> datastore) {
    return new DetectionTypeController(datastore);
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "scienceName";
  }

  @Override
  protected DetectionType createNewObject(boolean withUUID) {
    return new DetectionType(
        !withUUID ? null : UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected DetectionType setUniqueField(DetectionType object, String uniqueField) {
    return new DetectionType(
        object.uuid(),
        object.source(),
        uniqueField
    );
  }
}
