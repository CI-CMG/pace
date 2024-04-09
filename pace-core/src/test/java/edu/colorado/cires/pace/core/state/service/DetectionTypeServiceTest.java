package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.DetectionTypeRepository;
import edu.colorado.cires.pace.data.DetectionType;
import java.util.UUID;

class DetectionTypeServiceTest extends CrudServiceTest<DetectionType, DetectionTypeRepository> {

  @Override
  protected Class<DetectionTypeRepository> getRepositoryClass() {
    return DetectionTypeRepository.class;
  }

  @Override
  protected CRUDService<DetectionType> createService(DetectionTypeRepository repository) {
    return new DetectionTypeService(repository);
  }

  @Override
  protected DetectionType createNewObject() {
    return new DetectionType(
        UUID.randomUUID(),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.scienceName(), actual.scienceName());
    assertEquals(expected.scienceName(), actual.scienceName());
  }
}
