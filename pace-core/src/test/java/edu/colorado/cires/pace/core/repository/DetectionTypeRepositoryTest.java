package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.DetectionType;

class DetectionTypeRepositoryTest extends CrudRepositoryTest<DetectionType, String> {

  @Override
  protected UUIDProvider<DetectionType> getUUIDPRovider() {
    return DetectionType::getUUID;
  }

  @Override
  protected UniqueFieldProvider<DetectionType, String> getUniqueFieldProvider() {
    return DetectionType::getScienceName;
  }

  @Override
  protected UUIDSetter<DetectionType> getUUIDSetter() {
    return DetectionType::setUUID;
  }

  @Override
  protected UniqueFieldSetter<DetectionType, String> getUniqueFieldSetter() {
    return DetectionType::setScienceName;
  }

  @Override
  protected CRUDRepository<DetectionType, String> createRepository() {
    return new DetectionTypeRepository(createDatastore());
  }

  @Override
  protected DetectionType createNewObject(int suffix) {
    DetectionType detectionType = new DetectionType();
    detectionType.setScienceName(String.format("science-name-%s", suffix));
    detectionType.setUse(true);
    detectionType.setSource(String.format("source-%s", suffix));
    return detectionType;
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual) {
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getScienceName(), actual.getScienceName());
    assertEquals(expected.getSource(), actual.getSource());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
