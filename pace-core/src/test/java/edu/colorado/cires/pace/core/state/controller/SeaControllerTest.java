package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;
import java.util.function.Supplier;

class SeaControllerTest extends CRUDControllerTest<Sea, String> {

  @Override
  protected CRUDController<Sea, String> createController(CRUDService<Sea, String> service) {
    return new SeaController(service);
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
  protected Sea createNewObject() {
    Sea sea = new Sea();
    sea.setName(UUID.randomUUID().toString());
    sea.setUUID(UUID.randomUUID());
    sea.setUse(true);
    return sea;
  }
}
