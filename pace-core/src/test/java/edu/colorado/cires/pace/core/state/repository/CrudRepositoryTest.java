package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.ObjectWithUniqueField;
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
        map.put(object.uuid(), object);
        return object;
      }

      @Override
      public void delete(O object) {
        map.remove(object.uuid());
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
            .filter(o -> o.uniqueField().equals(uniqueField))
            .findFirst();
      }

      @Override
      public Stream<O> findAll() {
        return map.values().stream();
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
    assertNotNull(created.uuid());
    assertEquals(object.uniqueField(), created.uniqueField());
    assertObjectsEqual(object, created, false);
    
    O saved = map.get(created.uuid());
    assertNotNull(saved);
    assertEquals(created.uuid(), saved.uuid());
    assertEquals(created.uniqueField(), saved.uniqueField());
    assertObjectsEqual(created, saved, true);
  }
  
  @Test
  void testCreateUUIDNotNull() {
    O object = createNewObject(1);
    object = (O) object.copyWithNewUUID(UUID.randomUUID());

    O finalObject = object;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(finalObject));
    assertEquals(String.format(
        "uuid for new %s must not be defined", repository.getObjectName()
    ), exception.getMessage());
  }

  @Test
  void testCreateUniqueFieldNull() {
    O object = createNewObject(1);
    object = copyWithUpdatedUniqueField(object, null);

    O finalObject = object;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(finalObject));
    assertEquals(String.format(
        "%s must be defined for a new %s", repository.getUniqueFieldName(), repository.getObjectName()
    ), exception.getMessage());
  }
  
  @Test
  void testCreateNameConflict() throws Exception {
    O one = createNewObject(1);
    repository.create(one);

    O two = createNewObject(1);
    Exception exception = assertThrows(ConflictException.class, () -> repository.create(two));
    assertEquals(String.format(
        "%s with %s %s already exists", repository.getObjectName(), repository.getUniqueFieldName(), two.uniqueField() 
    ), exception.getMessage());
  }
  
  @Test
  void testGetByUniqueField() throws Exception {
    O object = repository.create(createNewObject(1));
    O result = repository.getByUniqueField(object.uniqueField());
    assertEquals(object.uuid(), result.uuid());
    assertEquals(object.uniqueField(), result.uniqueField());
    assertObjectsEqual(object, result, true);
  }
  
  @Test
  void testGetByUniqueFieldNotFound() {
    O object = createNewObject(1);
    Exception exception = assertThrows(NotFoundException.class, () -> repository.getByUniqueField(object.uniqueField()));
    assertEquals(String.format(
        "%s with %s %s not found", repository.getObjectName(), repository.getUniqueFieldName(), object.uniqueField()
    ), exception.getMessage());
  }
  
  @Test
  void testGetByUUID() throws Exception {
    O object = repository.create(createNewObject(1));
    O result = repository.getByUUID(object.uuid());
    assertEquals(object.uuid(), result.uuid());
    assertEquals(object.uniqueField(), result.uniqueField());
    assertObjectsEqual(object, result, true);
  }
  
  @Test
  void testGetByUUIDNotFound() {
    UUID uuid = UUID.randomUUID();
    
    Exception exception = assertThrows(NotFoundException.class, () -> repository.getByUUID(uuid));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getObjectName(), uuid
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
            .sorted(Comparator.comparing(ObjectWithUniqueField::uniqueField))
            .toList()
    );
  }
  
  @Test
  void testUpdate() throws Exception {
    O object = repository.create(createNewObject(1));
    O toUpdate = createNewObject(2);
    object = copyWithUpdatedUniqueField(object, toUpdate.uniqueField());
    O updated = repository.update(object.uuid(), object);
    assertEquals(object.uuid(), updated.uuid());
    assertEquals(object.uniqueField(), updated.uniqueField());
    assertObjectsEqual(object, updated, true);
    
    O saved = map.get(updated.uuid());
    assertNotNull(saved);
    assertEquals(updated.uuid(), saved.uuid());
    assertEquals(updated.uniqueField(), saved.uniqueField());
    assertObjectsEqual(updated, saved, true);
  }
  
  @Test
  void testUpdateUUIDsNotEqual() throws Exception {
    O object = repository.create(createNewObject(1));
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.update(UUID.randomUUID(), object));
    assertEquals(String.format(
        "%s uuid does not match argument uuid", repository.getObjectName() 
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateUniqueFieldNull() throws Exception {
    O object = repository.create(createNewObject(1));
    object = copyWithUpdatedUniqueField(object, null);
    O finalObject = object;
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.update(finalObject.uuid(), finalObject));
    assertEquals(String.format(
        "%s must be defined for updated %s", repository.getUniqueFieldName(), repository.getObjectName()
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNotFound() {
    O object = createNewObject(1);
    object = (O) object.copyWithNewUUID(UUID.randomUUID());

    O finalObject = object;
    Exception exception = assertThrows(NotFoundException.class, () ->  repository.update(finalObject.uuid(), finalObject));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getObjectName(), object.uuid()
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNameCollision() throws Exception {
    repository.create(createNewObject(1));
    O two = repository.create(createNewObject(2));
    
    O updated = createNewObject(1);
    updated = (O) updated.copyWithNewUUID(two.uuid());

    O finalUpdated = updated;
    Exception exception = assertThrows(ConflictException.class, () -> repository.update(finalUpdated.uuid(), finalUpdated));
    assertEquals(String.format(
        "%s with %s %s already exists", repository.getObjectName(), repository.getUniqueFieldName(), updated.uniqueField()
    ), exception.getMessage());
  }
  
  @Test
  void testDelete() throws Exception {
    O object = repository.create(createNewObject(1));
    repository.delete(object.uuid());
    assertNull(map.get(object.uuid()));
  }
  
  @Test
  void testDeleteNotFound() {
    UUID uuid = UUID.randomUUID();
    Exception exception = assertThrows(NotFoundException.class, () -> repository.delete(uuid));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getObjectName(), uuid
    ), exception.getMessage());
  }
  
  protected abstract O createNewObject(int suffix);
  
  protected abstract O copyWithUpdatedUniqueField(O object, String uniqueField);
  
  protected abstract void assertObjectsEqual(O expected, O actual, boolean checkUUID);

}
