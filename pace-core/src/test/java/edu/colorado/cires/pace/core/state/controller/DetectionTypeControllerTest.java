package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.UUID;
import java.util.function.Supplier;

class DetectionTypeControllerTest extends CRUDControllerTest<DetectionType, String> {

  @Override
  protected CRUDController<DetectionType, String> createController(Datastore<DetectionType, String> datastore) {
    return new DetectionTypeController(datastore);
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
  protected UniqueFieldSetter<DetectionType, String> getUniqueFieldSetter() {
    return DetectionType::setScienceName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "scienceName";
  }

  @Override
  protected DetectionType createNewObject(boolean withUUID) {
    DetectionType detectionType = new DetectionType();
    if (withUUID) {
      detectionType.setUUID(UUID.randomUUID());
    }
    detectionType.setUse(true);
    detectionType.setSource(UUID.randomUUID().toString());
    detectionType.setScienceName(UUID.randomUUID().toString());
    return detectionType;
  }
}
