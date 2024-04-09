package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.DetectionType;

class DetectionTypeRepositoryTest extends CrudRepositoryTest<DetectionType> {

  @Override
  protected CRUDRepository<DetectionType> createRepository() {
    return new DetectionTypeRepository(createDatastore());
  }

  @Override
  protected DetectionType createNewObject(int suffix) {
    return new DetectionType(
        null,
        String.format("science-name-%s", suffix),
        String.format("source-%s", suffix)
    );
  }

  @Override
  protected DetectionType copyWithUpdatedUniqueField(DetectionType object, String uniqueField) {
    return new DetectionType(
        object.uuid(),
        object.source(),
        uniqueField
    );
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
    assertEquals(expected.scienceName(), actual.scienceName());
    assertEquals(expected.scienceName(), actual.scienceName());
  }
}
