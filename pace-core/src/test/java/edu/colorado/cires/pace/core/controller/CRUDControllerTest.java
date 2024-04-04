package edu.colorado.cires.pace.core.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.ValidationException;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CRUDControllerTest<O, U> {
  
  private CRUDController<O, U> controller;
  private final CRUDService<O, U> service = mock(CRUDService.class);
  private final Validator<O> validator = mock(Validator.class);
  protected abstract CRUDController<O, U> createController(CRUDService<O, U> service, Validator<O> validator);
  
  protected abstract UniqueFieldProvider<O, U> getUniqueFieldProvider();
  protected abstract UUIDProvider<O> getUuidProvider();
  
  private final UniqueFieldProvider<O, U> uniqueFieldProvider = getUniqueFieldProvider();
  private final UUIDProvider<O> uuidProvider = getUuidProvider(); 
  
  @BeforeEach
  void beforeEach() {
    reset(service);
    reset(validator);
    controller = createController(service, validator);
  }
  
  @Test
  void testCreate() throws Exception {
    O object = createNewObject();
    when(validator.validate(object)).thenReturn(
        Collections.emptySet()
    );
    when(service.create(object)).thenReturn(object);
    
    controller.create(object);
    
    verify(service, times(1)).create(any());
  }
  
  @Test
  void testCreateValidationError() throws Exception {
    O object = createNewObject();
    
    Set<ConstraintViolation> violations = Set.of(
        new ConstraintViolation("test1", "message1"),
        new ConstraintViolation("test2", "message2")
    );
    when(validator.validate(object)).thenReturn(violations);
    
    ValidationException exception = assertThrows(ValidationException.class, () -> controller.create(object));
    assertEquals("Object validation failed", exception.getMessage());
    assertEquals(violations, exception.getViolations());
    
    verify(service, times(0)).create(any());
  }
  
  @Test
  void testGetByUniqueField() throws Exception {
    O object = createNewObject();
    
    when(service.getByUniqueField(uniqueFieldProvider.getUniqueField(object))).thenReturn(object);
    
    O result = controller.getByUniqueField(uniqueFieldProvider.getUniqueField(object));
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    
    verify(service, times(1)).getByUniqueField(any());
    verify(validator, times(0)).validate(any());
  }
  
  @Test
  void testGetByUUID() throws Exception {
    O object = createNewObject();
    
    when(service.getByUUID(uuidProvider.getUUID(object))).thenReturn(object);
    
    O result = controller.getByUUID(uuidProvider.getUUID(object));
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    
    verify(service, times(1)).getByUUID(any());
    verify(validator, times(0)).validate(any());
  }
  
  @Test
  void testReadAll() throws Exception {
    O object1 = createNewObject();
    O object2 = createNewObject();

    Stream<O> expected = Stream.of(
        object1, object2
    );
    
    when(service.readAll(Collections.emptyList())).thenReturn(expected);
    
    Stream<O> actual = controller.readAll(Collections.emptyList());
    
    assertEquals(List.of(object1, object2), actual.toList());
    verify(validator, times(0)).validate(any());
  }
  
  @Test
  void testUpdate() throws Exception {
    O object = createNewObject();
    when(validator.validate(object)).thenReturn(Collections.emptySet());
    when(service.update(uuidProvider.getUUID(object), object)).thenReturn(object);
    
    O result = controller.update(uuidProvider.getUUID(object), object);
    assertEquals(uuidProvider.getUUID(object), uuidProvider.getUUID(result));
    
    verify(service, times(1)).update(any(), any());
  }
  
  @Test
  void testUpdateValidationErrors() {
    O object = createNewObject();
    Set<ConstraintViolation> violations = Set.of(
        new ConstraintViolation("test1", "message1"),
        new ConstraintViolation("test2", "message2")
    );
    when(validator.validate(object)).thenReturn(violations);
    
    ValidationException exception = assertThrows(ValidationException.class, () -> controller.update(uuidProvider.getUUID(object), object));
    assertEquals("Object validation failed", exception.getMessage());
    assertEquals(violations, exception.getViolations());
  }
  
  @Test
  void testDelete() throws Exception {
    O object = createNewObject();
    
    controller.delete(uuidProvider.getUUID(object));
    
    verify(validator, times(0)).validate(any());
  }
  
  protected abstract O createNewObject();
}
