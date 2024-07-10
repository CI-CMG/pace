package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CRUDRepository<O extends ObjectWithUniqueField> {
  private final Logger LOGGER;

  private final Datastore<O> datastore;
  private final Validator validator;
  private final boolean writableUUID;
  
  public CRUDRepository(Datastore<O> datastore) {
    LOGGER = LoggerFactory.getLogger(this.getClass());
    this.datastore = datastore;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    this.writableUUID = false;
  }

  public CRUDRepository(Datastore<O> datastore, boolean writableUUID) {
    LOGGER = LoggerFactory.getLogger(this.getClass());
    this.datastore = datastore;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    this.writableUUID = writableUUID;
  }

  protected abstract O setUUID(O object, UUID uuid) throws BadArgumentException;
  
  public O create(O object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    validate(object);
    if (object.getUuid() != null && !writableUUID) {
      LOGGER.error("uuid for new {} must not be defined", getClassName());
      throw new BadArgumentException(String.format(
          "uuid for new %s must not be defined", getClassName()
      ));
    }
    String uniqueField = datastore.getUniqueFieldGetter().apply(object);
    if (datastore.findByUniqueField(uniqueField).isPresent()) {
      LOGGER.error("{} with {} = {} already exists", getClassName(), getUniqueFieldName(), uniqueField);
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getClassName(), getUniqueFieldName(), uniqueField
      ));
    }
    
    if (object.getUuid() != null && writableUUID) {
      if (datastore.findByUUID(object.getUuid()).isPresent()) {
        LOGGER.error("{} with uuid {} already exists", getClassName(), object.getUuid());
        throw new ConflictException(String.format(
            "%s with uuid %s already exists", getClassName(), object.getUuid()
        ));
      }
    }
    
    if (!writableUUID || object.getUuid() == null) {
      UUID uuid = UUID.randomUUID();
      LOGGER.debug("Generated new {} with uuid = {}", datastore.getClassName(), uuid);
      object = setUUID(object, uuid);
    }

    object = datastore.save(object);
    LOGGER.info("Created {} with {} = {}, uuid = {}", datastore.getClassName(), getUniqueFieldName(), uniqueField, object.getUuid());
    return object;
  }
  
  public O getByUniqueField(String uniqueField) throws DatastoreException, NotFoundException {
    O object = datastore.findByUniqueField(uniqueField).orElseThrow(
        () -> {
          LOGGER.error("{} with {} {} not found", getClassName(), getUniqueFieldName(), uniqueField);
          return new NotFoundException(String.format(
              "%s with %s %s not found", getClassName(), getUniqueFieldName(), uniqueField
          ));
        }
    );
    LOGGER.debug("Found {} with {} = {}", getClassName(), getUniqueFieldName(), uniqueField);
    return object;
  }
  
  public O getByUUID(UUID uuid) throws DatastoreException, NotFoundException {
    O object = datastore.findByUUID(uuid).orElseThrow(
        () -> {
          LOGGER.error("{} with uuid {} not found", getClassName(), uuid);
          return new NotFoundException(String.format(
              "%s with uuid %s not found", getClassName(), uuid
          ));
        }
    );
    LOGGER.debug("Found {} with uuid {}", getClassName(), uuid);
    return object;
  }
  
  public Stream<O> findAll() throws DatastoreException {
    LOGGER.debug("Listing all {} objects", getClassName());
    Function<O, String> uniqueFieldGetter = datastore.getUniqueFieldGetter();
    return datastore.findAll()
        .sorted((o1, o2) -> uniqueFieldGetter.apply(o1).compareToIgnoreCase(
            uniqueFieldGetter.apply(o2)
        ));
  }

  public O update(UUID uuid, O object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    validate(object);
    UUID objectUUID = object.getUuid();
    if (objectUUID == null) {
      LOGGER.error("{} uuid must be defined", getClassName());
      throw new BadArgumentException(String.format(
          "%s uuid must be defined", getClassName()
      ));
    }
    if (!objectUUID.equals(uuid) && !writableUUID) {
      LOGGER.error("{} uuid does not match argument uuid", getClassName());
      throw new BadArgumentException(String.format(
          "%s uuid does not match argument uuid", getClassName()
      ));
    }
    O existingObject = getByUUID(uuid);
    String newUniqueField = datastore.getUniqueFieldGetter().apply(object);
    String existingUniqueField = datastore.getUniqueFieldGetter().apply(existingObject);
    if (!newUniqueField.equals(existingUniqueField) && datastore.findByUniqueField(newUniqueField).isPresent()) {
      LOGGER.error("{} with {} = {} already exists", getClassName(), getUniqueFieldName(), newUniqueField);
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getClassName(), getUniqueFieldName(), newUniqueField
      ));
    }

    if (!object.getUuid().equals(uuid) && writableUUID) {
      if (datastore.findByUUID(object.getUuid()).isPresent()) {
        LOGGER.error("{} with uuid = {} already exists", getClassName(), object.getUuid());
        throw new ConflictException(String.format(
            "%s with uuid %s already exists", getClassName(), object.getUuid()
        ));
      }
    }
    
    object = datastore.save(object);
    LOGGER.info(
        "Updated {} with {} = {}, uuid = {}",
        datastore.getClassName(),
        getUniqueFieldName(),
        newUniqueField, object.getUuid()
    );
    
    if (writableUUID && (!uuid.equals(objectUUID))) {
      if (datastore.findByUUID(existingObject.getUuid()).isPresent()) {
        LOGGER.debug("{} with outdated uuid = {} will be removed", getClassName(), existingObject.getUuid());
        datastore.delete(existingObject);
      }
    }
    
    return object;
  }
  
  public void delete(UUID uuid) throws DatastoreException, NotFoundException {
    datastore.delete(
        getByUUID(uuid)
    );
    LOGGER.info("Deleted {} with uuid = {}", getClassName(), uuid);
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
      LOGGER.error("{} failed validation: {}", datastore.getClassName(), violations);
      throw new ConstraintViolationException(String.format(
          "%s validation failed", datastore.getClassName() 
      ), violations);
    }
  }
}
