package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.Datastore;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class CRUDRepository<O extends ObjectWithUniqueField> {
  private final Datastore<O> datastore;
  private final Validator validator;
  
  public CRUDRepository(Datastore<O> datastore) {
    this.datastore = datastore;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }
  
  protected abstract O setUUID(O object, UUID uuid);
  
  public O create(O object) throws Exception {
    validate(object);
    if (object.getUuid() != null) {
      throw new IllegalArgumentException(String.format(
          "uuid for new %s must not be defined", getClassName()
      ));
    }
    String uniqueField = datastore.getUniqueFieldGetter().apply(object);
    if (datastore.findByUniqueField(uniqueField).isPresent()) {
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getClassName(), getUniqueFieldName(), uniqueField
      ));
    }
    
    return datastore.save(setUUID(object, UUID.randomUUID()));
  }
  
  public O getByUniqueField(String uniqueField) throws Exception {
    return datastore.findByUniqueField(uniqueField).orElseThrow(
        () -> new NotFoundException(String.format(
            "%s with %s %s not found", getClassName(), getUniqueFieldName(), uniqueField
        ))
    );
  }
  
  public O getByUUID(UUID uuid) throws Exception {
    return datastore.findByUUID(uuid).orElseThrow(
        () -> new NotFoundException(String.format(
            "%s with uuid %s not found", getClassName(), uuid
        ))
    );
  }
  
  public Stream<O> findAll() throws Exception {
    return datastore.findAll();
  }

  public O update(UUID uuid, O object) throws Exception {
    validate(object);
    UUID objectUUID = object.getUuid();
    if (!objectUUID.equals(uuid)) {
      throw new IllegalArgumentException(String.format(
          "%s uuid does not match argument uuid", getClassName()
      ));
    }
    O existingObject = getByUUID(objectUUID);
    String newUniqueField = datastore.getUniqueFieldGetter().apply(object);
    String existingUniqueField = datastore.getUniqueFieldGetter().apply(existingObject);
    if (!newUniqueField.equals(existingUniqueField) && datastore.findByUniqueField(newUniqueField).isPresent()) {
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getClassName(), getUniqueFieldName(), newUniqueField
      ));
    }
    
    return datastore.save(object);
  }
  
  public void delete(UUID uuid) throws Exception {
    datastore.delete(
        getByUUID(uuid)
    );
  }
  
  public String getClassName() {
    return datastore.getClassName();
  }

  public String getUniqueFieldName() {
    return datastore.getUniqueFieldName();
  }
  
  private void validate(O object) throws ConstraintViolationException {
    Set<ConstraintViolation<O>> violations = validator.validate(object);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(String.format(
          "%s validation failed", datastore.getClassName() 
      ), violations);
    }
  }
}
