package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.function.Consumer;

public class DetectionTypeService extends CRUDService<DetectionType, String> {

  protected DetectionTypeService(CRUDRepository<DetectionType, String> detectionTypeRepository,
      Consumer<DetectionType> onSuccess, Consumer<Exception> onFailure) {
    super(detectionTypeRepository, onSuccess, onFailure);
  }
}
