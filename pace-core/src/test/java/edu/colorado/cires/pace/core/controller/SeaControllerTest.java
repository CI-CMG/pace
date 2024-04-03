package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Sea;
import java.util.UUID;

class SeaControllerTest extends CRUDControllerTest<Sea, String> {

  @Override
  protected CRUDController<Sea, String> createController(CRUDService<Sea, String> service, Validator<Sea> validator) {
    return new SeaController(service, validator);
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
  protected Sea createNewObject() {
    Sea sea = new Sea();
    sea.setName(UUID.randomUUID().toString());
    sea.setUUID(UUID.randomUUID());
    sea.setUse(true);
    return sea;
  }
}
