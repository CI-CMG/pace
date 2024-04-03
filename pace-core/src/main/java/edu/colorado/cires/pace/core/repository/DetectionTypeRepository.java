package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeRepository extends CRUDRepository<DetectionType, String> {

  protected DetectionTypeRepository(Datastore<DetectionType, String> datastore) {
    super(DetectionType::getUUID, DetectionType::getScienceName, DetectionType::setUUID, datastore);
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
