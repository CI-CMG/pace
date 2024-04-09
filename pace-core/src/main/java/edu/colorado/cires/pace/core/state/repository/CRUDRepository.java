package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class CRUDRepository<O extends ObjectWithUniqueField> {
  private final Datastore<O> datastore;

  protected CRUDRepository(Datastore<O> datastore) {
    this.datastore = datastore;
  }
  
  public O create(O object) throws Exception {
    if (object.uuid() != null) {
      throw new IllegalArgumentException(String.format(
          "uuid for new %s must not be defined", getObjectName()
      ));
    }
    String uniqueField = object.uniqueField();
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
    
    O objectWithUUID = (O) object.copyWithNewUUID(UUID.randomUUID());
    
    return datastore.save(objectWithUUID);
  }
  
  public O getByUniqueField(String uniqueField) throws Exception {
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
    UUID objectUUID = object.uuid();
    if (!objectUUID.equals(uuid)) {
      throw new IllegalArgumentException(String.format(
          "%s uuid does not match argument uuid", getObjectName()
      ));
    }
    O existingObject = getByUUID(objectUUID);
    String newUniqueField = object.uniqueField();
    if (newUniqueField == null) {
      throw new IllegalArgumentException(String.format(
          "%s must be defined for updated %s", getUniqueFieldName(), getObjectName()
      ));
    }
    String existingUniqueField = existingObject.uniqueField();
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
