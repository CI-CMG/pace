package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Sea;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

class SeaRepositoryTest extends CrudRepositoryTest<Sea, String> {

  @Override
  protected UUIDProvider<Sea> getUUIDPRovider() {
    return Sea::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Sea, String> getUniqueFieldProvider() {
    return Sea::getName;
  }

  @Override
  protected UUIDSetter<Sea> getUUIDSetter() {
    return Sea::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Sea, String> getUniqueFieldSetter() {
    return Sea::setName;
  }

  @Override
  protected CRUDRepository<Sea, String> createRepository() {
    return new SeaRepository() {
      @Override
      public Stream<Sea> findAll() {
        return findAllObjects();
      }

      @Override
      protected Sea save(Sea object) {
        return saveObject(object);
      }

      @Override
      protected void delete(Sea object) {
        deleteObject(object);
      }

      @Override
      protected Optional<Sea> findByUUID(UUID uuid) {
        return findObjectByUUID(uuid);
      }

      @Override
      protected Optional<Sea> findByUniqueField(String uniqueField) {
        return findObjectByUniqueField(uniqueField);
      }
    };
  }

  @Override
  protected Sea createNewObject(int suffix) {
    Sea sea = new Sea();
    sea.setUse(true);
    sea.setName(String.format("name-%s", suffix));
    return sea;
  }

  @Override
  protected void assertObjectsEqual(Sea expected, Sea actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
