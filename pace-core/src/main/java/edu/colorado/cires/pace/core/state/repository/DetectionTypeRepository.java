package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeRepository extends CRUDRepository<DetectionType> {

  public DetectionTypeRepository(Datastore<DetectionType> datastore) {
    super(datastore);
  }

  @Override
  protected String getObjectName() {
    return DetectionType.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "scienceName";
  }
}
