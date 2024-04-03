package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.DetectionType;

public abstract class DetectionTypeRepository extends CRUDRepository<DetectionType, String> {

  protected DetectionTypeRepository() {
    super(DetectionType::getUUID, DetectionType::getScienceName, DetectionType::setUUID);
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
