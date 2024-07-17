package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.repository.search.SeaSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.List;

class SeaRepositoryTest extends CrudRepositoryTest<Sea> {

  @Override
  protected CRUDRepository<Sea> createRepository() {
    return new SeaRepository(createDatastore());
  }

  @Override
  protected SearchParameters<Sea> createSearchParameters(List<Sea> objects) {
    return SeaSearchParameters.builder()
        .names(objects.stream().map(Sea::getName).toList())
        .build();
  }

  @Override
  protected Sea createNewObject(int suffix) {
    return Sea.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Sea copyWithUpdatedUniqueField(Sea object, String uniqueField) {
    return Sea.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }
}
