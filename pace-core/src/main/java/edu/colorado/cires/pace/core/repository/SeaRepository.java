package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.Sea;

public abstract class SeaRepository extends CRUDRepository<Sea, String> {

  protected SeaRepository() {
    super(Sea::getUUID, Sea::getName, Sea::setUUID);
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
