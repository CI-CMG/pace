package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.core.exception.ConflictException;
import edu.colorado.cires.pace.core.exception.NotFoundException;
import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.data.validation.ValidationException;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class CRUDRepository<O extends ObjectWithUniqueField> {
  private final Datastore<O> datastore;
  
  public CRUDRepository(Datastore<O> datastore) {
    this.datastore = datastore;
  }
  
  protected abstract O setUUID(O object, UUID uuid) throws ValidationException;
  
  public O create(O object) throws Exception {
    if (object.getUuid() != null) {
      throw new IllegalArgumentException(String.format(
          "uuid for new %s must not be defined", getClassName()
      ));
    }
    String uniqueField = object.getUniqueField();
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
  
  public Stream<O> findAll(List<Function<O, Boolean>> filters) throws Exception {
    return findAll()
        .filter(v -> {
          boolean filterValue = true;
          for (Function<O, Boolean> filter : filters) {
            filterValue = filter.apply(v);
          }
          return filterValue;
        });
  }

  public O update(UUID uuid, O object) throws Exception {
    UUID objectUUID = object.getUuid();
    if (!objectUUID.equals(uuid)) {
      throw new IllegalArgumentException(String.format(
          "%s uuid does not match argument uuid", getClassName()
      ));
    }
    O existingObject = getByUUID(objectUUID);
    String newUniqueField = object.getUniqueField();
    String existingUniqueField = existingObject.getUniqueField();
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
}
