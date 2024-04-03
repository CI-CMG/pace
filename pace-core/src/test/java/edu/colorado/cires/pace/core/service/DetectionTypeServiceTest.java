package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.DetectionType;

import edu.colorado.cires.pace.core.repository.UUIDProvider;
import java.util.UUID;
import java.util.function.Consumer;

class DetectionTypeServiceTest extends CrudServiceTest<DetectionType, String, DetectionTypeRepository> {

  @Override
  protected Class<DetectionTypeRepository> getRepositoryClass() {
    return DetectionTypeRepository.class;
  }

  @Override
  protected UniqueFieldProvider<DetectionType, String> getUniqueFieldProvider() {
    return DetectionType::getScienceName;
  }

  @Override
  protected UUIDProvider<DetectionType> getUUIDProvider() {
    return DetectionType::getUUID;
  }

  @Override
  protected CRUDService<DetectionType, String> createService(DetectionTypeRepository repository, Consumer<DetectionType> onSuccessHandler,
      Consumer<Exception> onFailureHandler) {
    return new DetectionTypeService(repository, onSuccessHandler, onFailureHandler);
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

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual) {

  }
}
