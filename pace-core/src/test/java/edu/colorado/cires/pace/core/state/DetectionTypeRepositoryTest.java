package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.validation.ValidationException;

class DetectionTypeRepositoryTest extends CrudRepositoryTest<DetectionType> {

  @Override
  protected CRUDRepository<DetectionType> createRepository() {
    return new DetectionTypeRepository(createDatastore());
  }

  @Override
  protected DetectionType createNewObject(int suffix) throws ValidationException {
    return DetectionType.builder()
        .scienceName(String.format("science-name-%s", suffix))
        .source(String.format("source-%s", suffix))
        .build();
  }

  @Override
  protected DetectionType copyWithUpdatedUniqueField(DetectionType object, String uniqueField) throws ValidationException {
    return DetectionType.builder()
        .uuid(object.getUuid())
        .source(object.getSource())
        .scienceName(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(DetectionType expected, DetectionType actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getScienceName(), actual.getScienceName());
    assertEquals(expected.getScienceName(), actual.getScienceName());
  }
}
