package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CrudRepositoryTest<O extends ObjectWithUniqueField> {
  
  protected final Map<UUID, O> map = new HashMap<>(0);
  
  protected CRUDRepository<O> repository;

  protected abstract CRUDRepository<O> createRepository();
  
  protected Datastore<O> createDatastore() {
    return new Datastore<>() {
      @Override
      public O save(O object) {
        map.put(object.getUuid(), object);
        return object;
      }

      @Override
      public void delete(O object) {
        map.remove(object.getUuid());
      }

      @Override
      public Optional<O> findByUUID(UUID uuid) {
        return Optional.ofNullable(
            map.get(uuid)
        );
      }

      @Override
      public Optional<O> findByUniqueField(String uniqueField) {
        return map.values().stream()
            .filter(o -> o.getUniqueField().equals(uniqueField))
            .findFirst();
      }

      @Override
      public Stream<O> findAll() {
        return map.values().stream();
      }

      @Override
      public String getUniqueFieldName() {
        return "test";
      }

      @Override
      public String getClassName() {
        return String.class.getSimpleName();
      }
    };
  }
  
  @BeforeEach
  void beforeEach() {
    repository = createRepository();
  } 
  
  @Test
  void testCreate() throws Exception {
    O object = createNewObject(1);
    O created = repository.create(object);
    assertNotNull(created.getUuid());
    assertEquals(object.getUniqueField(), created.getUniqueField());
    assertObjectsEqual(object, created, false);
    
    O saved = map.get(created.getUuid());
    assertNotNull(saved);
    assertEquals(created.getUuid(), saved.getUuid());
    assertEquals(created.getUniqueField(), saved.getUniqueField());
    assertObjectsEqual(created, saved, true);
  }
  
  @Test
  void testCreateUUIDNotNull() throws ValidationException {
    O object = createNewObject(1);
    object = repository.setUUID(object, UUID.randomUUID());

    O finalObject = object;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(finalObject));
    assertEquals(String.format(
        "uuid for new %s must not be defined", repository.getClassName()
    ), exception.getMessage());
  }
  
  @Test
  void testCreateNameConflict() throws Exception {
    O one = createNewObject(1);
    repository.create(one);

    O two = createNewObject(1);
    Exception exception = assertThrows(ConflictException.class, () -> repository.create(two));
    assertEquals(String.format(
        "%s with %s %s already exists", repository.getClassName(), repository.getUniqueFieldName(), two.getUniqueField() 
    ), exception.getMessage());
  }
  
  @Test
  void testGetByUniqueField() throws Exception {
    O object = repository.create(createNewObject(1));
    O result = repository.getByUniqueField(object.getUniqueField());
    assertEquals(object.getUuid(), result.getUuid());
    assertEquals(object.getUniqueField(), result.getUniqueField());
    assertObjectsEqual(object, result, true);
  }
  
  @Test
  void testGetByUniqueFieldNotFound() throws ValidationException {
    O object = createNewObject(1);
    Exception exception = assertThrows(NotFoundException.class, () -> repository.getByUniqueField(object.getUniqueField()));
    assertEquals(String.format(
        "%s with %s %s not found", repository.getClassName(), repository.getUniqueFieldName(), object.getUniqueField()
    ), exception.getMessage());
  }
  
  @Test
  void testGetByUUID() throws Exception {
    O object = repository.create(createNewObject(1));
    O result = repository.getByUUID(object.getUuid());
    assertEquals(object.getUuid(), result.getUuid());
    assertEquals(object.getUniqueField(), result.getUniqueField());
    assertObjectsEqual(object, result, true);
  }
  
  @Test
  void testGetByUUIDNotFound() {
    UUID uuid = UUID.randomUUID();
    
    Exception exception = assertThrows(NotFoundException.class, () -> repository.getByUUID(uuid));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getClassName(), uuid
    ), exception.getMessage());
  }
  
  @Test
  void testFindAll() throws Exception {
    O one = repository.create(createNewObject(1));
    O two = repository.create(createNewObject(2));
    
    assertEquals(
        List.of(
            one, two
        ),
        repository.findAll()
            .sorted(Comparator.comparing(ObjectWithUniqueField::getUniqueField))
            .toList()
    );
  }
  
  @Test
  void testUpdate() throws Exception {
    O object = repository.create(createNewObject(1));
    O toUpdate = createNewObject(2);
    object = copyWithUpdatedUniqueField(object, toUpdate.getUniqueField());
    O updated = repository.update(object.getUuid(), object);
    assertEquals(object.getUuid(), updated.getUuid());
    assertEquals(object.getUniqueField(), updated.getUniqueField());
    assertObjectsEqual(object, updated, true);
    
    O saved = map.get(updated.getUuid());
    assertNotNull(saved);
    assertEquals(updated.getUuid(), saved.getUuid());
    assertEquals(updated.getUniqueField(), saved.getUniqueField());
    assertObjectsEqual(updated, saved, true);
  }
  
  @Test
  void testUpdateUUIDsNotEqual() throws Exception {
    O object = repository.create(createNewObject(1));
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.update(UUID.randomUUID(), object));
    assertEquals(String.format(
        "%s uuid does not match argument uuid", repository.getClassName() 
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNotFound() throws ValidationException {
    O object = createNewObject(1);
    object = repository.setUUID(object, UUID.randomUUID());

    O finalObject = object;
    Exception exception = assertThrows(NotFoundException.class, () ->  repository.update(finalObject.getUuid(), finalObject));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getClassName(), object.getUuid()
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNameCollision() throws Exception {
    repository.create(createNewObject(1));
    O two = repository.create(createNewObject(2));
    
    O updated = createNewObject(1);
    updated = repository.setUUID(updated, two.getUuid());

    O finalUpdated = updated;
    Exception exception = assertThrows(ConflictException.class, () -> repository.update(finalUpdated.getUuid(), finalUpdated));
    assertEquals(String.format(
        "%s with %s %s already exists", repository.getClassName(), repository.getUniqueFieldName(), updated.getUniqueField()
    ), exception.getMessage());
  }
  
  @Test
  void testDelete() throws Exception {
    O object = repository.create(createNewObject(1));
    repository.delete(object.getUuid());
    assertNull(map.get(object.getUuid()));
  }
  
  @Test
  void testDeleteNotFound() {
    UUID uuid = UUID.randomUUID();
    Exception exception = assertThrows(NotFoundException.class, () -> repository.delete(uuid));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getClassName(), uuid
    ), exception.getMessage());
  }
  
  protected abstract O createNewObject(int suffix) throws ValidationException;
  
  protected abstract O copyWithUpdatedUniqueField(O object, String uniqueField) throws ValidationException;
  
  protected abstract void assertObjectsEqual(O expected, O actual, boolean checkUUID);

}
