package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Ship;
import edu.colorado.cires.pace.data.validation.ValidationException;

class ShipRepositoryTest extends CrudRepositoryTest<Ship> {

  @Override
  protected CRUDRepository<Ship> createRepository() {
    return new ShipRepository(createDatastore());
  }

  @Override
  protected Ship createNewObject(int suffix) throws ValidationException {
    return Ship.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Ship copyWithUpdatedUniqueField(Ship object, String uniqueField) throws ValidationException {
    return Ship.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }
}
