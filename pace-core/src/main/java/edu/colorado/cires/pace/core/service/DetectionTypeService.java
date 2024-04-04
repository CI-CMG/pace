package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeService extends CRUDService<DetectionType, String> {

  public DetectionTypeService(CRUDRepository<DetectionType, String> detectionTypeRepository) {
    super(detectionTypeRepository);
  }
}
