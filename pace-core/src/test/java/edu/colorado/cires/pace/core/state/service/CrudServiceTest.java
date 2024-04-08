package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import java.util.Collections;
import java.util.List;
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

  protected abstract CRUDService<O, U> createService(R repository);
  
  protected abstract O createNewObject();
  
  @BeforeEach
  void beforeEach() {
    uniqueFieldProvider = getUniqueFieldProvider();
    uuidProvider = getUUIDProvider();
    service = createService(repository);
  }
  
  @Test
  void testCreate() throws Exception {
    O object = createNewObject();
    
    when(repository.create(object)).thenReturn(object);
    
    O result = service.create(object);
    assertObjectsEqual(object, result);
  }
  
  @Test
  void testCreateException() throws Exception {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.create(object)).thenThrow(exception);
    
    assertThrows(IllegalArgumentException.class, () -> service.create(object));
  }
  
  @Test
  void testGetByUniqueField() throws Exception {
    O object = createNewObject();
    
    when(repository.getByUniqueField(any())).thenReturn(object);
    
    O result = service.getByUniqueField(uniqueFieldProvider.getUniqueField(object));
    assertObjectsEqual(object, result);
  }
  
  @Test
  void testGetByUniqueFieldException() throws Exception {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.getByUniqueField(any())).thenThrow(exception);
    
    assertThrows(IllegalArgumentException.class, () -> service.getByUniqueField(uniqueFieldProvider.getUniqueField(object)));
  }
  
  @Test
  void testGetByUUID() throws Exception {
    O object = createNewObject();
    when(repository.getByUUID(any())).thenReturn(object);
    
    O result = service.getByUUID(uuidProvider.getUUID(object));
    assertObjectsEqual(object, result);
  }
  
  @Test
  void testGetByUUIDException() throws Exception {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.getByUUID(any())).thenThrow(exception);
    
    assertThrows(IllegalArgumentException.class, () -> service.getByUUID(uuidProvider.getUUID(object)));
  }
  
  @Test
  void testReadAll() throws Exception {
    O object1 = createNewObject();
    O object2 = createNewObject();
    
    when(repository.findAll()).thenReturn(Stream.of(
        object1, object2
    ));
    
    assertEquals(
        List.of(object1, object2),
        service.readAll(Collections.emptyList()).toList()
    );
  }
  
  @Test
  void testReadAllFiltered() throws Exception {
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
  }
  
  @Test
  void testUpdate() throws Exception {
    O object = createNewObject();
    
    when(repository.update(uuidProvider.getUUID(object), object)).thenReturn(object);
    
    O result = service.update(uuidProvider.getUUID(object), object);
    
    assertObjectsEqual(result, object);
  }
  
  @Test
  void testUpdateError() throws Exception {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    when(repository.update(uuidProvider.getUUID(object), object)).thenThrow(exception);
    
    assertThrows(IllegalArgumentException.class, () -> service.update(uuidProvider.getUUID(object), object));
  }
  
  @Test
  void testDelete() throws Exception {
    O object = createNewObject();
    doNothing().when(repository).delete(uuidProvider.getUUID(object));
    
    service.delete(uuidProvider.getUUID(object));
  }
  
  @Test
  void testDeleteException() throws Exception {
    O object = createNewObject();
    Exception exception = new IllegalArgumentException("Bad argument");
    doThrow(exception).when(repository).delete(uuidProvider.getUUID(object));
    
    assertThrows(IllegalArgumentException.class, () -> service.delete(uuidProvider.getUUID(object)));
  }
  
  protected abstract void assertObjectsEqual(O expected, O actual);

}