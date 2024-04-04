package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CrudRepositoryTest<O, U extends Comparable<U>> {
  
  @FunctionalInterface
  protected interface UniqueFieldSetter<O, U> {
    void setUniqueField(O object, U uniqueField);
  } 
  
  protected final Map<UUID, O> map = new HashMap<>(0);
  
  protected CRUDRepository<O, U> repository;
  protected UUIDProvider<O> uuidProvider;
  protected UniqueFieldProvider<O, U> uniqueFieldProvider;
  protected UUIDSetter<O> uuidSetter;
  protected UniqueFieldSetter<O, U> uniqueFieldSetter;
  
  protected abstract UUIDProvider<O> getUUIDPRovider();
  protected abstract UniqueFieldProvider<O, U> getUniqueFieldProvider();
  protected abstract UUIDSetter<O> getUUIDSetter();
  protected abstract UniqueFieldSetter<O, U> getUniqueFieldSetter();

  protected abstract CRUDRepository<O, U> createRepository();
  
  protected Datastore<O, U> createDatastore() {
    return new Datastore<>() {
      @Override
      public O save(O object) {
        map.put(uuidProvider.getUUID(object), object);
        return object;
      }

      @Override
      public void delete(O object) {
        map.remove(uuidProvider.getUUID(object));
      }

      @Override
      public Optional<O> findByUUID(UUID uuid) {
        return Optional.ofNullable(
            map.get(uuid)
        );
      }

      @Override
      public Optional<O> findByUniqueField(U uniqueField) {
        return map.values().stream()
            .filter(o -> uniqueFieldProvider.getUniqueField(o).equals(uniqueField))
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
    uuidProvider = getUUIDPRovider();
    uniqueFieldProvider = getUniqueFieldProvider();
    uuidSetter = getUUIDSetter();
    uniqueFieldSetter = getUniqueFieldSetter();
    
    repository = createRepository();
  } 
  
  @Test
  void testCreate() throws Exception {
    O object = createNewObject(1);
    O created = repository.create(object);
    assertNotNull(uuidProvider.getUUID(created));
    assertEquals(uniqueFieldProvider.getUniqueField(object), uniqueFieldProvider.getUniqueField(created));
    assertObjectsEqual(object, created);
    
    O saved = map.get(uuidProvider.getUUID(created));
    assertNotNull(saved);
    assertEquals(uuidProvider.getUUID(created), uuidProvider.getUUID(saved));
    assertEquals(uniqueFieldProvider.getUniqueField(created), uniqueFieldProvider.getUniqueField(saved));
    assertObjectsEqual(created, saved);
  }
  
  @Test
  void testCreateUUIDNotNull() {
    O object = createNewObject(1);
    uuidSetter.setUUID(object, UUID.randomUUID());
    
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(object));
    assertEquals(String.format(
        "uuid for new %s must not be defined", repository.getObjectName()
    ), exception.getMessage());
  }

  @Test
  void testCreateUniqueFieldNull() {
    O object = createNewObject(1);
    uniqueFieldSetter.setUniqueField(object, null);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.create(object));
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
        "%s with %s %s already exists", repository.getObjectName(), repository.getUniqueFieldName(), uniqueFieldProvider.getUniqueField(two) 
    ), exception.getMessage());
  }
  
  @Test
  void testGetByUniqueField() throws Exception {
    O object = repository.create(createNewObject(1));
    O result = repository.getByUniqueField(uniqueFieldProvider.getUniqueField(object));
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    assertEquals(uniqueFieldProvider.getUniqueField(object), uniqueFieldProvider.getUniqueField(result));
    assertObjectsEqual(object, result);
  }
  
  @Test
  void testGetByUniqueFieldNotFound() {
    O object = createNewObject(1);
    Exception exception = assertThrows(NotFoundException.class, () -> repository.getByUniqueField(uniqueFieldProvider.getUniqueField(object)));
    assertEquals(String.format(
        "%s with %s %s not found", repository.getObjectName(), repository.getUniqueFieldName(), uniqueFieldProvider.getUniqueField(object)
    ), exception.getMessage());
  }
  
  @Test
  void testGetByUUID() throws Exception {
    O object = repository.create(createNewObject(1));
    O result = repository.getByUUID(uuidProvider.getUUID(object));
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    assertEquals(uniqueFieldProvider.getUniqueField(object), uniqueFieldProvider.getUniqueField(result));
    assertObjectsEqual(object, result);
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
            .sorted((o1, o2) -> uniqueFieldProvider.getUniqueField(o1).compareTo(uniqueFieldProvider.getUniqueField(o2)))
            .toList()
    );
  }
  
  @Test
  void testUpdate() throws Exception {
    O object = repository.create(createNewObject(1));
    O toUpdate = createNewObject(2);
    uniqueFieldSetter.setUniqueField(object, uniqueFieldProvider.getUniqueField(toUpdate));
    O updated = repository.update(uuidProvider.getUUID(object), object);
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(updated));
    assertEquals(uniqueFieldProvider.getUniqueField(object), uniqueFieldProvider.getUniqueField(updated));
    assertObjectsEqual(object, updated);
    
    O saved = map.get(uuidProvider.getUUID(updated));
    assertNotNull(saved);
    assertEquals(uuidProvider.getUUID(updated), uuidProvider.getUUID(saved));
    assertEquals(uniqueFieldProvider.getUniqueField(updated), uniqueFieldProvider.getUniqueField(saved));
    assertObjectsEqual(updated, saved);
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
    uniqueFieldSetter.setUniqueField(object, null);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> repository.update(uuidProvider.getUUID(object), object));
    assertEquals(String.format(
        "%s must be defined for updated %s", repository.getUniqueFieldName(), repository.getObjectName()
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNotFound() {
    O object = createNewObject(1);
    uuidSetter.setUUID(object, UUID.randomUUID());
    
    Exception exception = assertThrows(NotFoundException.class, () ->  repository.update(uuidProvider.getUUID(object), object));
    assertEquals(String.format(
        "%s with uuid %s not found", repository.getObjectName(), uuidProvider.getUUID(object)
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNameCollision() throws Exception {
    repository.create(createNewObject(1));
    O two = repository.create(createNewObject(2));
    
    O updated = createNewObject(1);
    uuidSetter.setUUID(updated, uuidProvider.getUUID(two));
    
    Exception exception = assertThrows(ConflictException.class, () -> repository.update(uuidProvider.getUUID(updated), updated));
    assertEquals(String.format(
        "%s with %s %s already exists", repository.getObjectName(), repository.getUniqueFieldName(), uniqueFieldProvider.getUniqueField(updated)
    ), exception.getMessage());
  }
  
  @Test
  void testDelete() throws Exception {
    O object = repository.create(createNewObject(1));
    repository.delete(uuidProvider.getUUID(object));
    assertNull(map.get(uuidProvider.getUUID(object)));
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
  
  protected abstract void assertObjectsEqual(O expected, O actual);

}
