package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.exception.ValidationException;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CRUDController<O, U> {

  private final CRUDService<O, U> service;
  private final Validator<O> validator;
  protected abstract Validator<O> getValidator();
  protected abstract CRUDService<O, U> createService(Datastore<O, U> datastore, Datastore<?, ?>... additionalDataStores);

  protected CRUDController(Datastore<O, U> datastore, Datastore<?, ?>... additionalDataStores) {
    this.service = createService(datastore, additionalDataStores);
    this.validator = getValidator();
  }
  
  public O create(O object) throws Exception {
    return service.create(validate(object));
  }
  
  public O getByUniqueField(U uniqueField) throws Exception {
    return service.getByUniqueField(uniqueField);
  }
  
  public O getByUUID(UUID uuid) throws Exception {
    return service.getByUUID(uuid);
  }
  
  public Stream<O> readAll(List<Function<O, Boolean>> filters) throws Exception {
    return service.readAll(filters);
  }
  
  public O update(UUID uuid, O object) throws Exception {
    return service.update(uuid, validate(object));
  }
  
  public void delete(UUID uuid) throws Exception {
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
