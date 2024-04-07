package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Platform;

public class PlatformRepository extends CRUDRepository<Platform, String> {

  public PlatformRepository(Datastore<Platform, String> datastore) {
    super(Platform::getUUID, Platform::getName, Platform::setUUID, datastore);
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
