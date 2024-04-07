package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.DetectionType;

public class DetectionTypeService extends CRUDService<DetectionType, String> {

  public DetectionTypeService(CRUDRepository<DetectionType, String> detectionTypeRepository) {
    super(detectionTypeRepository);
  }
}
