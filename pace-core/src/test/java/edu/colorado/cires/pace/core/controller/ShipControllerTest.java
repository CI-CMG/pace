package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Ship;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

class ShipControllerTest extends CRUDControllerTest<Ship, String> {

  @Override
  protected CRUDController<Ship, String> createController(CRUDService<Ship, String> service, Validator<Ship> validator,
      Consumer<Set<ConstraintViolation>> onValidationErrorHandler) {
    return new ShipController(service, validator, onValidationErrorHandler);
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
  protected Ship createNewObject() {
    Ship ship = new Ship();
    ship.setUUID(UUID.randomUUID());
    ship.setUse(true);
    ship.setName(UUID.randomUUID().toString());
    return ship;
  }
}
