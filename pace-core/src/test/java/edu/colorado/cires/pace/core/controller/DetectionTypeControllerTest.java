package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.UUID;

class DetectionTypeControllerTest extends CRUDControllerTest<DetectionType, String> {

  @Override
  protected CRUDController<DetectionType, String> createController(CRUDService<DetectionType, String> service, Validator<DetectionType> validator) {
    return new DetectionTypeController(service, validator);
  }

  @Override
  protected UniqueFieldProvider<DetectionType, String> getUniqueFieldProvider() {
    return DetectionType::getScienceName;
  }

  @Override
  protected UUIDProvider<DetectionType> getUuidProvider() {
    return DetectionType::getUUID;
  }

  @Override
  protected DetectionType createNewObject() {
    DetectionType detectionType = new DetectionType();
    detectionType.setUUID(UUID.randomUUID());
    detectionType.setUse(true);
    detectionType.setSource(UUID.randomUUID().toString());
    detectionType.setScienceName(UUID.randomUUID().toString());
    return detectionType;
  }
}
