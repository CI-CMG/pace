package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.core.datastore.Datastore;
import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class CRUDRepository<O, U> {

  private final UUIDProvider<O> uuidProvider;
  private final UniqueFieldProvider<O, U> uniqueFieldProvider;
  private final UUIDSetter<O> uuidSetter;
  private final Datastore<O, U> datastore;

  protected CRUDRepository(UUIDProvider<O> uuidProvider, UniqueFieldProvider<O, U> uniqueFieldProvider, UUIDSetter<O> uuidSetter, Datastore<O, U> datastore) {
    this.uuidProvider = uuidProvider;
    this.uniqueFieldProvider = uniqueFieldProvider;
    this.uuidSetter = uuidSetter;
    this.datastore = datastore;
  }
  
  public O create(O object) throws Exception {
    if (uuidProvider.getUUID(object) != null) {
      throw new IllegalArgumentException(String.format(
          "uuid for new %s must not be defined", getObjectName()
      ));
    }
    U uniqueField = uniqueFieldProvider.getUniqueField(object);
    if (uniqueField == null) {
      throw new IllegalArgumentException(String.format(
          "%s must be defined for a new %s", getUniqueFieldName(), getObjectName()
      ));
    }
    if (datastore.findByUniqueField(uniqueField).isPresent()) {
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getObjectName(), getUniqueFieldName(), uniqueField
      ));
    }
    
    uuidSetter.setUUID(object, UUID.randomUUID());
    
    return datastore.save(object);
  }
  
  public O getByUniqueField(U uniqueField) throws Exception {
    return datastore.findByUniqueField(uniqueField).orElseThrow(
        () -> new NotFoundException(String.format(
            "%s with %s %s not found", getObjectName(), getUniqueFieldName(), uniqueField
        ))
    );
  }
  
  public O getByUUID(UUID uuid) throws Exception {
    return datastore.findByUUID(uuid).orElseThrow(
        () -> new NotFoundException(String.format(
            "%s with uuid %s not found", getObjectName(), uuid
        ))
    );
  }
  
  public Stream<O> findAll() throws Exception {
    return datastore.findAll();
  }

  public O update(UUID uuid, O object) throws Exception {
    UUID objectUUID = uuidProvider.getUUID(object);
    if (!objectUUID.equals(uuid)) {
      throw new IllegalArgumentException(String.format(
          "%s uuid does not match argument uuid", getObjectName()
      ));
    }
    O existingObject = getByUUID(objectUUID);
    U newUniqueField = uniqueFieldProvider.getUniqueField(object);
    if (newUniqueField == null) {
      throw new IllegalArgumentException(String.format(
          "%s must be defined for updated %s", getUniqueFieldName(), getObjectName()
      ));
    }
    U existingUniqueField = uniqueFieldProvider.getUniqueField(existingObject);
    if (!newUniqueField.equals(existingUniqueField) && datastore.findByUniqueField(newUniqueField).isPresent()) {
      throw new ConflictException(String.format(
          "%s with %s %s already exists", getObjectName(), getUniqueFieldName(), newUniqueField
      ));
    }
    
    return datastore.save(object);
  }
  
  public void delete(UUID uuid) throws Exception {
    datastore.delete(
        getByUUID(uuid)
    );
  }
  
  protected abstract String getObjectName();
  protected abstract String getUniqueFieldName();
}
