package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Platform;

public class PlatformRepository extends CRUDRepository<Platform> {

  public PlatformRepository(Datastore<Platform> datastore) {
    super(datastore);
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
