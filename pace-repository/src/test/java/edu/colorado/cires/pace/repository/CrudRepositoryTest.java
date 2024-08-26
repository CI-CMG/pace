package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CrudRepositoryTest<O extends AbstractObject> {
  
  protected final Map<UUID, O> map = new HashMap<>(0);
  
  protected CRUDRepository<O> repository;

  protected abstract CRUDRepository<O> createRepository();
  
  protected abstract String getUniqueFieldName();
  
  protected abstract Class<O> getObjectClass();
  
  protected Datastore<O> createDatastore() {
    return createDatastore(map, getObjectClass(), getUniqueFieldName());
  }
  
  protected <C extends AbstractObject> Datastore<C> createDatastore(Map<UUID, C> map, Class<C> clazz, String uniqueFieldName) {
    return new Datastore<>() {
      @Override
      public C save(C object) {
        map.put(object.getUuid(), object);
        return object;
      }

      @Override
      public void delete(C object) {
        map.remove(object.getUuid());
      }

      @Override
      public Optional<C> findByUUID(UUID uuid) {
        return Optional.ofNullable(
            map.get(uuid)
        );
      }

      @Override
      public Optional<C> findByUniqueField(String uniqueField) {
        return map.values().stream()
            .filter(o -> o.getUniqueField().equals(uniqueField))
            .findFirst();
      }

      @Override
      public Stream<C> findAll() {
        return map.values().stream();
      }

      @Override
      public String getUniqueFieldName() {
        return uniqueFieldName;
      }

      @Override
      public String getClassName() {
        return clazz.getSimpleName();
      }
    };
  }

  private SearchParameters<O> createSearchParameters(List<O> objects, List<Boolean> visibilityStates) {
    return SearchParameters.<O>builder()
        .uniqueFields(objects.stream().map(AbstractObject::getUniqueField).toList())
        .visibilityStates(visibilityStates)
        .build();
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
  void testCreateConstrainViolation() {
    O object = createNewObject(1);
    object = copyWithUpdatedUniqueField(object, "");

    O finalObject = object;
    ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> repository.create(finalObject));
    assertEquals(String.format(
        "%s validation failed", repository.getClassName()
    ), exception.getMessage());

    Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
    assertEquals(1, constraintViolations.size());
    ConstraintViolation<?> constraintViolation = constraintViolations.iterator().next();
    assertEquals(getUniqueFieldName(), constraintViolation.getPropertyPath().toString());
    assertEquals("must not be blank", constraintViolation.getMessage());
  }
  
  @Test
  void testCreateUUIDNotNull() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O object = createNewObject(1);
    object = (O) object.setUuid(UUID.randomUUID());

    O finalObject = object;
    Exception exception = assertThrows(BadArgumentException.class, () -> repository.create(finalObject));
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
        "%s already exists", two.getUniqueField()
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
  void testGetByUniqueFieldNotFound() {
    O object = createNewObject(1);
    Exception exception = assertThrows(NotFoundException.class, () -> repository.getByUniqueField(object.getUniqueField()));
    assertEquals(String.format(
        "%s with %s = %s not found", repository.getClassName(), repository.getUniqueFieldName(), object.getUniqueField()
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
        "%s with uuid = %s not found", repository.getClassName(), uuid
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
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );
  }
  
  @Test
  void testSearch() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O one = repository.create(createNewObject(1));
    repository.create(createNewObject(2));
    O three = repository.create(createNewObject(3));
    repository.create(createNewObject(4));

    SearchParameters<O> searchParameters = createSearchParameters(List.of(one, three), List.of(true));
    assertEquals(
        List.of(one, three),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );
  }

  @Test
  void testSearchVisibility() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O one = repository.create((O) createNewObject(1).setVisible(false));
    O two = repository.create(createNewObject(2));
    O three = repository.create((O) createNewObject(3).setVisible(false));
    O four = repository.create(createNewObject(4));

    SearchParameters<O> searchParameters = createSearchParameters(List.of(one, three), List.of(true));
    assertEquals(
        Collections.emptyList(),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );

    searchParameters = createSearchParameters(List.of(one, three), List.of(false));
    assertEquals(
        List.of(one, three),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );
    
    searchParameters = createSearchParameters(List.of(two, four), List.of(true));
    assertEquals(
        List.of(two, four),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );
    searchParameters = createSearchParameters(List.of(two), List.of(true));
    assertEquals(
        List.of(two),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );
    
    searchParameters = createSearchParameters(List.of(one, four), List.of());
    assertEquals(
        List.of(one, four),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
            .toList()
    );
  }

  @Test
  void testSearchNoParameters() throws BadArgumentException, ConflictException, NotFoundException, DatastoreException {
    O one = repository.create(createNewObject(1));
    O two = repository.create(createNewObject(2));
    O three = repository.create(createNewObject(3));
    O four = repository.create(createNewObject(4));

    SearchParameters<O> searchParameters = createSearchParameters(Collections.emptyList(), List.of(true));
    assertEquals(
        List.of(one, two, three, four),
        repository.search(searchParameters)
            .sorted(Comparator.comparing(AbstractObject::getUniqueField))
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
  void testUpdateNullUUID() {
    O object = createNewObject(1);
    Exception exception = assertThrows(BadArgumentException.class, () -> repository.update(UUID.randomUUID(), object));
    assertEquals(String.format(
        "%s uuid must be defined", repository.getClassName()
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateUUIDsNotEqual() throws Exception {
    O object = repository.create(createNewObject(1));
    Exception exception = assertThrows(BadArgumentException.class, () -> repository.update(UUID.randomUUID(), object));
    assertEquals(String.format(
        "%s uuid does not match argument uuid", repository.getClassName() 
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNotFound() {
    O object = createNewObject(1);
    object = (O) object.setUuid(UUID.randomUUID());

    O finalObject = object;
    Exception exception = assertThrows(NotFoundException.class, () ->  repository.update(finalObject.getUuid(), finalObject));
    assertEquals(String.format(
        "%s with uuid = %s not found", repository.getClassName(), object.getUuid()
    ), exception.getMessage());
  }
  
  @Test
  void testUpdateNameCollision() throws Exception {
    repository.create(createNewObject(1));
    O two = repository.create(createNewObject(2));
    
    O updated = createNewObject(1);
    updated = (O) updated.setUuid(two.getUuid());

    O finalUpdated = updated;
    Exception exception = assertThrows(ConflictException.class, () -> repository.update(finalUpdated.getUuid(), finalUpdated));
    assertEquals(String.format(
        "%s already exists", updated.getUniqueField()
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
        "%s with uuid = %s not found", repository.getClassName(), uuid
    ), exception.getMessage());
  }
  
  protected abstract O createNewObject(int suffix);
  
  protected abstract O copyWithUpdatedUniqueField(O object, String uniqueField);
  
  protected abstract void assertObjectsEqual(O expected, O actual, boolean checkUUID);

}
