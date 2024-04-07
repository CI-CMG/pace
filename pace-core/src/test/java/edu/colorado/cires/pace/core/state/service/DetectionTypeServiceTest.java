package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.UUID;

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
  protected CRUDService<DetectionType, String> createService(DetectionTypeRepository repository) {
    return new DetectionTypeService(repository);
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
