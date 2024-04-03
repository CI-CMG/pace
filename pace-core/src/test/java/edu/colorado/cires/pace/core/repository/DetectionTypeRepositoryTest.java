package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.DetectionType;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    return new DetectionTypeRepository() {
      @Override
      public Stream<DetectionType> findAll() {
        return findAllObjects();
      }

      @Override
      protected DetectionType save(DetectionType object) {
        return saveObject(object);
      }

      @Override
      protected void delete(DetectionType object) {
        deleteObject(object);
      }

      @Override
      protected Optional<DetectionType> findByUUID(UUID uuid) {
        return findObjectByUUID(uuid);
      }

      @Override
      protected Optional<DetectionType> findByUniqueField(String uniqueField) {
        return findObjectByUniqueField(uniqueField);
      }
    };
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
