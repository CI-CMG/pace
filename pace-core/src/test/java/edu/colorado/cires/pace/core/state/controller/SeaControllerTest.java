package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;
import java.util.function.Supplier;

class SeaControllerTest extends CRUDControllerTest<Sea, String> {

  @Override
  protected CRUDController<Sea, String> createController(Datastore<Sea, String> datastore) {
    return new SeaController(datastore);
  }

  @Override
  protected UniqueFieldProvider<Sea, String> getUniqueFieldProvider() {
    return Sea::getName;
  }

  @Override
  protected UUIDProvider<Sea> getUuidProvider() {
    return Sea::getUUID;
  }

  @Override
  protected UniqueFieldSetter<Sea, String> getUniqueFieldSetter() {
    return Sea::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
  }

  @Override
  protected Sea createNewObject(boolean withUUID) {
    Sea sea = new Sea();
    sea.setName(UUID.randomUUID().toString());
    if (withUUID) {
      sea.setUUID(UUID.randomUUID());
    }
    sea.setUse(true);
    return sea;
  }
}
