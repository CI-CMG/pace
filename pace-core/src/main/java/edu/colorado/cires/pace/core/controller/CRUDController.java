package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

abstract class CRUDController<O, U> {
  
  private static final String VALIDATION_FAILED_MESSAGE = "Object validation failed";
  
  private final CRUDService<O, U> service;
  private final Validator<O> validator;
  private final Consumer<Set<ConstraintViolation>> onValidationError;

  protected CRUDController(CRUDService<O, U> service, Validator<O> validator, Consumer<Set<ConstraintViolation>> onValidationError) {
    this.service = service;
    this.validator = validator;
    this.onValidationError = onValidationError;
  }
  
  public O create(O object) {
    return validate(object).map(
        service::create
    ).orElseThrow(
        () -> new IllegalStateException(VALIDATION_FAILED_MESSAGE)
    );
  }
  
  public O getByUniqueField(U uniqueField) {
    return service.getByUniqueField(uniqueField);
  }
  
  public O getByUUID(UUID uuid) {
    return service.getByUUID(uuid);
  }
  
  public Stream<O> readAll(List<Function<O, Boolean>> filters) {
    return service.readAll(filters);
  }
  
  public O update(UUID uuid, O object) {
    return validate(object).map(
        o -> service.update(uuid, o)
    ).orElseThrow(
        () -> new IllegalStateException(VALIDATION_FAILED_MESSAGE)
    );
  }
  
  public void delete(UUID uuid) {
    service.delete(uuid);
  }
  
  private Optional<O> validate(O object) {
    Set<ConstraintViolation> violations = validator.validate(object);
    if (violations.isEmpty()) {
      return Optional.of(object);
    } else {
      onValidationError.accept(violations);
      return Optional.empty();
    }
  }
}
