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

/**
 * CRUDRepository allows creating, reading, updating, and deleting objects to and from a
 * storage location
 * @param <O>
 */
public abstract class CRUDRepository<O extends AbstractObject> {
  private final Logger LOGGER;

  private final Datastore<O> datastore;
  private final Validator validator;
  private final boolean writableUUID;

  /**
   * Creates a CRUD repository with defaulting unwritable uuid
   * @param datastore data-holding structure
   */
  public CRUDRepository(Datastore<O> datastore) {
    LOGGER = LoggerFactory.getLogger(this.getClass());
    this.datastore = datastore;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    this.writableUUID = false;
  }

  /**
   * Creates a CRUD with option to make the uuid writeable or not
   * @param datastore data-holding structure
   * @param writableUUID determines whether the uuid is writeable or not
   */
  public CRUDRepository(Datastore<O> datastore, boolean writableUUID) {
    LOGGER = LoggerFactory.getLogger(this.getClass());
    this.datastore = datastore;
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    this.writableUUID = writableUUID;
  }

  /**
   * Creates an object and stores it in th repository if the object is valid and not a duplicate
   * @param object object to be saved to the repository
   * @return O returns object which was provided after any alterations
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of error where uuid already exists
   * @throws NotFoundException thrown in case of object where object cannot be found
   * @throws BadArgumentException thrown if uuid is provided but uuid is not writeable
   */
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
      LOGGER.error("{} already exists", uniqueField);
      throw new ConflictException(String.format(
          "%s already exists", uniqueField
      ), datastore.findByUniqueField(uniqueField).orElse(null), object);
    }
    
    if (object.getUuid() != null) {
      if (datastore.findByUUID(object.getUuid()).isPresent()) {
        LOGGER.error("{} with uuid {} already exists", getClassName(), object.getUuid());
        throw new ConflictException(String.format(
            "%s with uuid %s already exists", getClassName(), object.getUuid()
        ), datastore.findByUniqueField(uniqueField).orElse(null), object);
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

  /**
   * Returns the relevant object relative to the provided unique field
   * @param uniqueField identifiable field of object to search by
   * @return O matching object
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws NotFoundException thrown in case of error finding object
   */
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

  /**
   * Returns the relevant object relative to the provided uuid
   * @param uuid uuid to search by
   * @return O object identified by uuid
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws NotFoundException thrown in case of error finding object
   * @throws BadArgumentException thrown in case of bad argument
   */
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

  /**
   * Returns all objects in the datastore
   * @return Stream of objects in datastore
   * @throws DatastoreException thrown in case of error accessing datastore
   */
  public Stream<O> findAll() throws DatastoreException {
    LOGGER.debug("Listing all {} objects", getClassName());
    return datastore.findAll();
  }

  /**
   * Filters objects in datastore to find those matching the provided search parameters
   * @param searchParameters filters to apply
   * @return Stream of objects matching with search parameters in the datastore
   * @throws DatastoreException thrown in case of error interacting with datastore
   */
  public Stream<O> search(SearchParameters<O> searchParameters) throws DatastoreException {
    return findAll()
        .filter(searchParameters::matches);
  }

  /**
   * Updates an object in the datastore with the provided uuid to the values of the provided object
   * @param uuid uuid to identify object to update by
   * @param object updated object
   * @return O object after update
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of object update causing collision between unique fields
   * @throws NotFoundException thrown in case of object with provided uuid not existing
   * @throws BadArgumentException thrown in case of bad argument
   */
  public O update(UUID uuid, O object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    validate(object);
    UUID objectUUID = object.getUuid();
    checkUUIDNotNull(objectUUID);
    checkUUIDsEqual(objectUUID, uuid);
    O existingObject = getByUUID(uuid);
    String newUniqueField = object.getUniqueField();
    String existingUniqueField = existingObject.getUniqueField();
    if (!newUniqueField.equals(existingUniqueField) && datastore.findByUniqueField(newUniqueField).isPresent()) {
      LOGGER.error("{} already exists", newUniqueField);
      throw new ConflictException(String.format(
          "%s already exists", newUniqueField
      ), datastore.findByUUID(uuid).orElse(null), object);
    }

    if (!object.getUuid().equals(uuid)) {
      if (datastore.findByUUID(object.getUuid()).isPresent()) {
        LOGGER.error("{} with uuid = {} already exists", getClassName(), object.getUuid());
        throw new ConflictException(String.format(
            "%s with uuid = %s already exists", getClassName(), object.getUuid()
        ), datastore.findByUUID(uuid).orElse(null), object);
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

  /**
   * Deletes the object with the matching uuid from the datastore
   * @param uuid uuid of object to delete
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws NotFoundException thrown in case of uuid not matching an object in the datastore
   * @throws BadArgumentException thrown in case of bad argument
   */
  public void delete(UUID uuid) throws DatastoreException, NotFoundException, BadArgumentException {
    datastore.delete(
        getByUUID(uuid)
    );
    LOGGER.info("Deleted {} with uuid = {}", getClassName(), uuid);
  }

  /**
   * Returns the class name
   * @return String class name
   */
  public String getClassName() {
    return datastore.getClassName();
  }

  /**
   * Returns unique field name
   * @return String unique field name
   */
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
