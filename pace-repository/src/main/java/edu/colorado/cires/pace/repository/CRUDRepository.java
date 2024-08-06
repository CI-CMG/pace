package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CRUDRepository<O extends AbstractObject> {
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
  
  public O create(O object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    validate(object);
    if (object.getUuid() != null && !writableUUID) {
      LOGGER.error("uuid for new {} must not be defined", getClassName());
      throw new BadArgumentException(String.format(
          "uuid for new %s must not be defined", getClassName()
      ));
    }
    String uniqueField = object.getUniqueField();
    if (datastore.findByUniqueField(uniqueField).isPresent()) {
      LOGGER.error("{} with {} = {} already exists", getClassName(), getUniqueFieldName(), uniqueField);
      throw new ConflictException(String.format(
          "%s with %s = %s already exists", getClassName(), getUniqueFieldName(), uniqueField
      ));
    }
    
    if (object.getUuid() != null) {
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
      object = (O) object.setUuid(uuid);
    }

    object = datastore.save(object);
    LOGGER.info("Created {} with {} = {}, uuid = {}", datastore.getClassName(), getUniqueFieldName(), uniqueField, object.getUuid());
    return object;
  }
  
  public O getByUniqueField(String uniqueField) throws DatastoreException, NotFoundException {
    O object = datastore.findByUniqueField(uniqueField).orElseThrow(
        () -> {
          LOGGER.error("{} with {} = {} not found", getClassName(), getUniqueFieldName(), uniqueField);
          return new NotFoundException(String.format(
              "%s with %s = %s not found", getClassName(), getUniqueFieldName(), uniqueField
          ));
        }
    );
    LOGGER.debug("Found {} with {} = {}", getClassName(), getUniqueFieldName(), uniqueField);
    return object;
  }
  
  public O getByUUID(UUID uuid) throws DatastoreException, NotFoundException, BadArgumentException {
    checkUUIDNotNull(uuid);
    O object = datastore.findByUUID(uuid).orElseThrow(
        () -> {
          LOGGER.error("{} with uuid = {} not found", getClassName(), uuid);
          return new NotFoundException(String.format(
              "%s with uuid = %s not found", getClassName(), uuid
          ));
        }
    );
    LOGGER.debug("Found {} with uuid = {}", getClassName(), uuid);
    return object;
  }
  
  public Stream<O> findAll() throws DatastoreException {
    LOGGER.debug("Listing all {} objects", getClassName());
    return datastore.findAll();
  }
  
  public Stream<O> search(SearchParameters<O> searchParameters) throws DatastoreException {
    return datastore.findAll()
        .filter(searchParameters::matches);
  }

  public O update(UUID uuid, O object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    validate(object);
    UUID objectUUID = object.getUuid();
    checkUUIDNotNull(objectUUID);
    checkUUIDsEqual(objectUUID, uuid);
    O existingObject = getByUUID(uuid);
    String newUniqueField = object.getUniqueField();
    String existingUniqueField = existingObject.getUniqueField();
    if (!newUniqueField.equals(existingUniqueField) && datastore.findByUniqueField(newUniqueField).isPresent()) {
      LOGGER.error("{} with {} = {} already exists", getClassName(), getUniqueFieldName(), newUniqueField);
      throw new ConflictException(String.format(
          "%s with %s = %s already exists", getClassName(), getUniqueFieldName(), newUniqueField
      ));
    }

    if (!object.getUuid().equals(uuid)) {
      if (datastore.findByUUID(object.getUuid()).isPresent()) {
        LOGGER.error("{} with uuid = {} already exists", getClassName(), object.getUuid());
        throw new ConflictException(String.format(
            "%s with uuid = %s already exists", getClassName(), object.getUuid()
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
      LOGGER.debug("{} with outdated uuid = {} will be removed", getClassName(), existingObject.getUuid());
      datastore.delete(existingObject);
    }
    
    return object;
  }

  protected void checkUUIDsEqual(UUID objectUUID, UUID uuid) throws BadArgumentException {
    if (!objectUUID.equals(uuid) && !writableUUID) {
      LOGGER.error("{} uuid does not match argument uuid", getClassName());
      throw new BadArgumentException(String.format(
          "%s uuid does not match argument uuid", getClassName()
      ));
    }
  }

  public void delete(UUID uuid) throws DatastoreException, NotFoundException, BadArgumentException {
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
  
  protected void validate(O object) throws ConstraintViolationException {
    Set<ConstraintViolation<O>> violations = validator.validate(object);
    if (!violations.isEmpty()) {
      LOGGER.error("{} failed validation: {}", datastore.getClassName(), violations);
      throw new ConstraintViolationException(String.format(
          "%s validation failed", datastore.getClassName() 
      ), violations);
    }
  }
  
  protected void checkUUIDNotNull(UUID objectUUID) throws BadArgumentException {
    if (objectUUID == null) {
      LOGGER.error("{} uuid must be defined", getClassName());
      throw new BadArgumentException(String.format(
          "%s uuid must be defined", getClassName()
      ));
    }
  }
}
