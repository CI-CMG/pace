package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.validation.ValidationException;

class SeaRepositoryTest extends CrudRepositoryTest<Sea> {

  @Override
  protected CRUDRepository<Sea> createRepository() {
    return new SeaRepository(createDatastore());
  }

  @Override
  protected Sea createNewObject(int suffix) throws ValidationException {
    return Sea.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Sea copyWithUpdatedUniqueField(Sea object, String uniqueField) throws ValidationException {
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