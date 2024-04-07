package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Platform;
import java.util.UUID;
import java.util.function.Supplier;

class PlatformControllerTest extends CRUDControllerTest<Platform, String> {

  @Override
  protected CRUDController<Platform, String> createController(CRUDService<Platform, String> service) {
    return new PlatformController(service);
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
  protected UniqueFieldSetter<Platform, String> getUniqueFieldSetter() {
    return Platform::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
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
