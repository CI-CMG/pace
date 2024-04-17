package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Ship;
import java.util.function.Function;

class ShipRepositoryTest extends CrudRepositoryTest<Ship> {

  @Override
  protected CRUDRepository<Ship> createRepository() {
    return new ShipRepository(createDatastore());
  }

  @Override
  protected Function<Ship, String> uniqueFieldGetter() {
    return Ship::getName;
  }

  @Override
  protected Ship createNewObject(int suffix) {
    return Ship.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Ship copyWithUpdatedUniqueField(Ship object, String uniqueField) {
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
