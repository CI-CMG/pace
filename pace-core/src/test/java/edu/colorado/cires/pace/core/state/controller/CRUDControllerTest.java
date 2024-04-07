package edu.colorado.cires.pace.core.state.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.exception.ValidationException;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CRUDControllerTest<O, U> {

  @FunctionalInterface
  protected interface UniqueFieldSetter<O, U> {
    void setUniqueField(O object, U uniqueField);
  }

  private CRUDController<O, U> controller;
  private final Datastore<O, U> datastore = mock(Datastore.class);
  protected abstract CRUDController<O, U> createController(Datastore<O, U> datastore) throws Exception;
  
  protected abstract UniqueFieldProvider<O, U> getUniqueFieldProvider();
  protected abstract UUIDProvider<O> getUuidProvider();
  protected abstract UniqueFieldSetter<O, U> getUniqueFieldSetter();
  protected abstract Supplier<String> getUniqueFieldName();
  
  
  private final UniqueFieldProvider<O, U> uniqueFieldProvider = getUniqueFieldProvider();
  private final UUIDProvider<O> uuidProvider = getUuidProvider(); 
  private final UniqueFieldSetter<O, U> uniqueFieldSetter = getUniqueFieldSetter();
  
  @BeforeEach
  void beforeEach() throws Exception {
    reset(datastore);
    controller = createController(datastore);
  }
  
  @Test
  void testCreate() throws Exception {
    O object = createNewObject(false);
    when(datastore.save(object)).thenReturn(object);
    
    controller.create(object);
    
    verify(datastore, times(1)).save(any());
  }
  
  @Test
  void testCreateValidationError() throws Exception {
    O object = createNewObject();
    uniqueFieldSetter.setUniqueField(object, null);
    
    Set<ConstraintViolation> violations = Set.of(
        new ConstraintViolation(getUniqueFieldName().get(), String.format(
            "%s must not be blank", getUniqueFieldName().get()
        ))
    );
    
    ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(object));
    assertEquals("Object validation failed", exception.getMessage());
    assertEquals(violations, exception.getViolations());
    
    verify(datastore, times(0)).save(any());
  }
  
  @Test
  void testGetByUniqueField() throws Exception {
    O object = createNewObject();
    
    when(datastore.findByUniqueField(uniqueFieldProvider.getUniqueField(object))).thenReturn(Optional.of(object));
    
    O result = controller.getByUniqueField(uniqueFieldProvider.getUniqueField(object));
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    
    verify(datastore, times(1)).findByUniqueField(any());
  }
  
  @Test
  void testGetByUUID() throws Exception {
    O object = createNewObject();
    
    when(datastore.findByUUID(uuidProvider.getUUID(object))).thenReturn(Optional.of(object));
    
    O result = controller.getByUUID(uuidProvider.getUUID(object));
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    
    verify(datastore, times(1)).findByUUID(any());
  }
  
  @Test
  void testReadAll() throws Exception {
    O object1 = createNewObject();
    O object2 = createNewObject();

    Stream<O> expected = Stream.of(
        object1, object2
    );
    
    when(datastore.findAll()).thenReturn(expected);
    
    Stream<O> actual = controller.readAll(Collections.emptyList());
    
    assertEquals(List.of(object1, object2), actual.toList());
  }
  
  @Test
  void testUpdate() throws Exception {
    O object = createNewObject();
    when(datastore.save(object)).thenReturn(object);
    when(datastore.findByUUID(uuidProvider.getUUID(object))).thenReturn(Optional.of(object));
    O result = controller.update(uuidProvider.getUUID(object), object);
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    
    verify(datastore, times(1)).save(any());
  }
  
  @Test
  void testUpdateValidationErrors() {
    O object = createNewObject();
    uniqueFieldSetter.setUniqueField(object, null);
    Set<ConstraintViolation> violations = Set.of(
        new ConstraintViolation(getUniqueFieldName().get(), String.format(
            "%s must not be blank", getUniqueFieldName().get()
        ))
    );
    
    ValidationException exception = assertThrows(ValidationException.class, () -> controller.update(uuidProvider.getUUID(object), object));
    assertEquals("Object validation failed", exception.getMessage());
    assertEquals(violations, exception.getViolations());
  }
  
  @Test
  void testDelete() throws Exception {
    O object = createNewObject();
    
    when(datastore.findByUUID(uuidProvider.getUUID(object))).thenReturn(Optional.of(object));
    
    controller.delete(uuidProvider.getUUID(object));
  }
  
  protected abstract O createNewObject(boolean withUUID);
  
  private O createNewObject() {
    return createNewObject(true);
  }
}
