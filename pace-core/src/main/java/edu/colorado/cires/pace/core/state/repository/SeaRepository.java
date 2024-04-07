package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Sea;

public class SeaRepository extends CRUDRepository<Sea, String> {

  public SeaRepository(Datastore<Sea, String> datastore) {
    super(Sea::getUUID, Sea::getName, Sea::setUUID, datastore);
  }

  @Override
  protected String getObjectName() {
    return Sea.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
