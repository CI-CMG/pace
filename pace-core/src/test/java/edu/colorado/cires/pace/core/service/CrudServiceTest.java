package edu.colorado.cires.pace.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CrudServiceTest<O, U, R extends CRUDRepository<O, U>> {
  
  private CRUDService<O, U> service;
  protected abstract Class<R> getRepositoryClass();
  private final R repository = mock(getRepositoryClass());
  private UniqueFieldProvider<O, U> uniqueFieldProvider;
  private UUIDProvider<O> uuidProvider;
  
  protected abstract UniqueFieldProvider<O, U> getUniqueFieldProvider();
  protected abstract UUIDProvider<O> getUUIDProvider();

  protected abstract CRUDService<O, U> createService(R repository, Consumer<O> onSuccessHandler, Consumer<Exception> onFailureHandler);
  
  private final List<Exception> errors = new ArrayList<>(0);
  protected final List<O> successes = new ArrayList<>(0);
  
  protected abstract O createNewObject();
  
  @BeforeEach
  void beforeEach() {
    successes.clear();
    errors.clear();
    
    uniqueFieldProvider = getUniqueFieldProvider();
    uuidProvider = getUUIDProvider();
    Consumer<O> onSuccessHandler = successes::add;
    Consumer<Exception> onFailureHandler = errors::add;
    service = createService(repository, onSuccessHandler, onFailureHandler);
  }
  
  @Test
  void testCreate() throws ConflictException {
    O object = createNewObject();
    
    when(repository.create(object)).thenReturn(object);
    
    O result = service.create(object);
    
    assertEquals(1, successes.size());
    assertEquals(0, errors.size());
    assertObjectsEqual(object, successes.get(0));
    assertObjectsEqual(object, result);
  }
  
  @Test
  void testCreateException() throws ConflictException {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.create(object)).thenThrow(exception);
    
    assertThrows(IllegalStateException.class, () -> service.create(object));
    
    assertEquals(1, errors.size());
    assertEquals(exception, errors.get(0));
    assertEquals(0, successes.size());
  }
  
  @Test
  void testGetByUniqueField() throws NotFoundException {
    O object = createNewObject();
    
    when(repository.getByUniqueField(any())).thenReturn(object);
    
    O result = service.getByUniqueField(uniqueFieldProvider.getUniqueField(object));
    assertObjectsEqual(object, result);
    assertEquals(0, errors.size());
    assertEquals(0, successes.size());
  }
  
  @Test
  void testGetByUniqueFieldException() throws NotFoundException {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.getByUniqueField(any())).thenThrow(exception);
    
    assertThrows(IllegalStateException.class, () -> service.getByUniqueField(uniqueFieldProvider.getUniqueField(object)));
    
    assertEquals(0, successes.size());
    assertEquals(1, errors.size());
    assertEquals(exception, errors.get(0));
  }
  
  @Test
  void testGetByUUID() throws NotFoundException {
    O object = createNewObject();
    when(repository.getByUUID(any())).thenReturn(object);
    
    O result = service.getByUUID(uuidProvider.getUUID(object));
    
    assertEquals(0, errors.size());
    assertEquals(0, successes.size());
    
    assertObjectsEqual(object, result);
  }
  
  @Test
  void testGetByUUIDException() throws NotFoundException {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.getByUUID(any())).thenThrow(exception);
    
    assertThrows(IllegalStateException.class, () -> service.getByUUID(uuidProvider.getUUID(object)));
    
    assertEquals(0, successes.size());
    assertEquals(1, errors.size());
    assertEquals(exception, errors.get(0));
  }
  
  @Test
  void testReadAll() {
    O object1 = createNewObject();
    O object2 = createNewObject();
    
    when(repository.findAll()).thenReturn(Stream.of(
        object1, object2
    ));
    
    assertEquals(
        List.of(object1, object2),
        service.readAll(Collections.emptyList()).toList()
    );
    
    assertEquals(0, errors.size());
    assertEquals(0, successes.size());
  }
  
  @Test
  void testReadAllFiltered() {
    O object1 = createNewObject();
    O object2 = createNewObject();
    
    when(repository.findAll()).thenReturn(Stream.of(
        object1, object2
    ));

    assertEquals(
        Collections.emptyList(),
        service.readAll(Collections.singletonList(
            (o) -> uuidProvider.getUUID(o).toString().equals("TEST")
        )).toList()
    );

    assertEquals(0, successes.size());
    assertEquals(0, errors.size());
  }
  
  @Test
  void testUpdate() throws ConflictException, NotFoundException {
    O object = createNewObject();
    
    when(repository.update(uuidProvider.getUUID(object), object)).thenReturn(object);
    
    O result = service.update(uuidProvider.getUUID(object), object);
    
    assertObjectsEqual(result, object);
    assertEquals(0, errors.size());
    assertEquals(1, successes.size());
    assertObjectsEqual(result, successes.get(0));
  }
  
  @Test
  void testUpdateError() throws ConflictException, NotFoundException {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.update(uuidProvider.getUUID(object), object)).thenThrow(exception);
    
    assertThrows(IllegalStateException.class, () -> service.update(uuidProvider.getUUID(object), object));
    
    assertEquals(0, successes.size());
    assertEquals(1, errors.size());
    assertEquals(exception, errors.get(0));
  }
  
  @Test
  void testDelete() throws NotFoundException {
    O object = createNewObject();
    when(repository.delete(uuidProvider.getUUID(object))).thenReturn(object);
    
    service.delete(uuidProvider.getUUID(object));
    
    assertEquals(0, errors.size());
    assertEquals(1, successes.size());
    assertObjectsEqual(object, successes.get(0));
  }
  
  @Test
  void testDeleteException() throws NotFoundException {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.delete(uuidProvider.getUUID(object))).thenThrow(exception);
    
    assertThrows(IllegalStateException.class, () -> service.delete(uuidProvider.getUUID(object)));
    
    assertEquals(0, successes.size());
    assertEquals(1, errors.size());
    assertEquals(exception, errors.get(0));
  }
  
  protected abstract void assertObjectsEqual(O expected, O actual);

}
