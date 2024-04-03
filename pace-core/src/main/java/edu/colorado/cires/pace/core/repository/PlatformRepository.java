package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.Platform;

public abstract class PlatformRepository extends CRUDRepository<Platform, String> {

  protected PlatformRepository() {
    super(Platform::getUUID, Platform::getName, Platform::setUUID);
  }

  @Override
  protected String getObjectName() {
    return Platform.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
