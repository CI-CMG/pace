package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Sea;
import java.util.function.Function;

class SeaRepositoryTest extends CrudRepositoryTest<Sea> {

  @Override
  protected CRUDRepository<Sea> createRepository() {
    return new SeaRepository(createDatastore());
  }

  @Override
  protected Function<Sea, String> uniqueFieldGetter() {
    return Sea::getName;
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
