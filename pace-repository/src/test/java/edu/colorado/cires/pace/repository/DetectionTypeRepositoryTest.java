package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.repository.search.DetectionTypeSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.List;

class DetectionTypeRepositoryTest extends CrudRepositoryTest<DetectionType> {

  @Override
  protected CRUDRepository<DetectionType> createRepository() {
    return new DetectionTypeRepository(createDatastore());
  }

  @Override
  protected SearchParameters<DetectionType> createSearchParameters(List<DetectionType> objects) {
    return DetectionTypeSearchParameters.builder()
        .sources(objects.stream().map(DetectionType::getSource).toList())
        .build();
  }

  @Override
  protected DetectionType createNewObject(int suffix) {
    return DetectionType.builder()
        .scienceName(String.format("science-name-%s", suffix))
        .source(String.format("source-%s", suffix))
        .build();
  }

  @Override
  protected DetectionType copyWithUpdatedUniqueField(DetectionType object, String uniqueField) {
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
