package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.ValidationException;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.core.service.CRUDService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class CRUDController<O, U> {

  private final CRUDService<O, U> service;
  private final Validator<O> validator;

  protected CRUDController(CRUDService<O, U> service, Validator<O> validator) {
    this.service = service;
    this.validator = validator;
  }
  
  public O create(O object) throws ConflictException, IllegalArgumentException, ValidationException {
    return service.create(validate(object));
  }
  
  public O getByUniqueField(U uniqueField) throws NotFoundException {
    return service.getByUniqueField(uniqueField);
  }
  
  public O getByUUID(UUID uuid) throws NotFoundException {
    return service.getByUUID(uuid);
  }
  
  public Stream<O> readAll(List<Function<O, Boolean>> filters) {
    return service.readAll(filters);
  }
  
  public O update(UUID uuid, O object) throws NotFoundException, IllegalArgumentException, ConflictException, ValidationException {
    return service.update(uuid, validate(object));
  }
  
  public void delete(UUID uuid) throws NotFoundException {
    service.delete(uuid);
  }
  
  private O validate(O object) throws ValidationException {
    Set<ConstraintViolation> violations = validator.validate(object);
    if (violations.isEmpty()) {
      return object;
    } else {
      throw new ValidationException(violations);
    }
  }
}
