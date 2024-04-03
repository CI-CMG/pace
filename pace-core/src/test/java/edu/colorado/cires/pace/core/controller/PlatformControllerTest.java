package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Platform;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

class PlatformControllerTest extends CRUDControllerTest<Platform, String> {

  @Override
  protected CRUDController<Platform, String> createController(CRUDService<Platform, String> service, Validator<Platform> validator,
      Consumer<Set<ConstraintViolation>> onValidationErrorHandler) {
    return new PlatformController(service, validator, onValidationErrorHandler);
  }

  @Override
  protected UniqueFieldProvider<Platform, String> getUniqueFieldProvider() {
    return Platform::getName;
  }

  @Override
  protected UUIDProvider<Platform> getUuidProvider() {
    return Platform::getUUID;
  }

  @Override
  protected Platform createNewObject() {
    Platform platform = new Platform();
    platform.setName(UUID.randomUUID().toString());
    platform.setUUID(UUID.randomUUID());
    platform.setUse(true);
    return platform;
  }
}
