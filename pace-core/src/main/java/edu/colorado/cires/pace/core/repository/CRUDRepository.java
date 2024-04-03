package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class CRUDRepository<O, U> {

  private final UUIDProvider<O> uuidProvider;
  private final UniqueFieldProvider<O, U> uniqueFieldProvider;
  private final UUIDSetter<O> uuidSetter;

  protected CRUDRepository(UUIDProvider<O> uuidProvider, UniqueFieldProvider<O, U> uniqueFieldProvider, UUIDSetter<O> uuidSetter) {
    this.uuidProvider = uuidProvider;
    this.uniqueFieldProvider = uniqueFieldProvider;
    this.uuidSetter = uuidSetter;
  }
  
  public O create(O object) throws IllegalArgumentException, ConflictException {
    if (uuidProvider.getUUID(object) != null) {
      throw new IllegalArgumentException(String.format(
          "uuid for new %s must not be defined", getObjectName()
      ));
    }
    U uniqueField = uniqueFieldProvider.getUniqueField(object);
    if (findByUniqueField(uniqueField).isPresent()) {
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getObjectName(), getUniqueFieldName(), uniqueField
      ));
    }
    
    uuidSetter.setUUID(object, UUID.randomUUID());
    
    return save(object);
  }
  
  public O getByUniqueField(U uniqueField) throws NotFoundException {
    return findByUniqueField(uniqueField).orElseThrow(
        () -> new NotFoundException(String.format(
            "%s with %s %s not found", getObjectName(), getUniqueFieldName(), uniqueField
        ))
    );
  }
  
  public O getByUUID(UUID uuid) throws NotFoundException {
    return findByUUID(uuid).orElseThrow(
        () -> new NotFoundException(String.format(
            "%s with uuid %s not found", getObjectName(), uuid
        ))
    );
  }
  
  public abstract Stream<O> findAll();
  
  public O update(UUID uuid, O object) throws NotFoundException, IllegalArgumentException, ConflictException {
    UUID objectUUID = uuidProvider.getUUID(object);
    if (!objectUUID.equals(uuid)) {
      throw new IllegalArgumentException(String.format(
          "%s uuid does not match argument uuid", getObjectName()
      ));
    }
    O existingObject = getByUUID(objectUUID);
    U newUniqueField = uniqueFieldProvider.getUniqueField(object);
    U existingUniqueField = uniqueFieldProvider.getUniqueField(existingObject);
    if (!newUniqueField.equals(existingUniqueField) && findByUniqueField(newUniqueField).isPresent()) {
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getObjectName(), getUniqueFieldName(), newUniqueField
      ));
    }
    
    return save(object);
  }
  
  public void delete(UUID uuid) throws NotFoundException {
    delete(
        getByUUID(uuid)
    );
  }
  
  protected abstract O save(O object);
  protected abstract void delete(O object);
  
  protected abstract String getObjectName();
  protected abstract String getUniqueFieldName();
  
  protected abstract Optional<O> findByUUID(UUID uuid);
  protected abstract Optional<O> findByUniqueField(U uniqueField);
}
