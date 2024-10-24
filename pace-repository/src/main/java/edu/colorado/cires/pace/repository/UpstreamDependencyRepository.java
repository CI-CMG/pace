package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.Datastore;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * UpstreamDependencyRepository extends CRUDRepository
 * @param <O> object type
 * @param <D> dependency object type
 */
public abstract class UpstreamDependencyRepository<O extends AbstractObject, D extends AbstractObject> extends CRUDRepository<O> {
  
  private final Datastore<D> dependencyDatastore;
  
  protected abstract boolean dependencyAppliesToObject(D dependency, O object);
  protected abstract String getDependentObjectUniqueFieldName();
  protected abstract Class<D> getDependentObjectClass();
  protected abstract D applyObjectToDependentObjects(O original, O updated, D dependency);

  /**
   * Creates an upstream dependency repository
   * @param datastore holds objects
   * @param writableUUID indicates if uuid is writeable
   * @param dependencyDatastore holds dependency objects
   */
  public UpstreamDependencyRepository(Datastore<O> datastore, boolean writableUUID, Datastore<D> dependencyDatastore) {
    super(datastore, writableUUID);
    this.dependencyDatastore = dependencyDatastore;
  }

  /**
   * Updates the object identified by provided uuid to provided object
   * @param uuid uuid to identify object to update by
   * @param object updated object
   * @return O provided object after any changes
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws ConflictException thrown in case of duplicate uuid
   * @throws NotFoundException thrown in case of object not being findable
   * @throws BadArgumentException thrown in case of bad argument
   */
  @Override
  public O update(UUID uuid, O object) throws DatastoreException, ConflictException, NotFoundException, BadArgumentException {
    checkUUIDNotNull(object.getUuid());
    checkUUIDsEqual(object.getUuid(), uuid);
    O original = getByUUID(uuid);
    object = super.update(uuid, object);
    if (!original.getUniqueField().equals(object.getUniqueField())) {
      RuntimeException runtimeException = new RuntimeException();
      O finalObject = object;
      getDependentObjects(original)
          .map(d -> applyObjectToDependentObjects(original, finalObject, d))
          .forEach(d -> {
            try {
              dependencyDatastore.save(d);
            } catch (DatastoreException e) {
              runtimeException.addSuppressed(e);
            }
          });
      if (runtimeException.getSuppressed().length > 0) {
        throw runtimeException;
      }
    }
    return object;
  }

  /**
   * Removes the object indicated by uuid from datastore
   * @param uuid uuid of object to delete
   * @throws DatastoreException thrown in case of error interacting with datastore
   * @throws NotFoundException thrown in case of uuid not tracking to object
   * @throws BadArgumentException thrown in case of bad argument
   */
  @Override
  public void delete(UUID uuid) throws DatastoreException, NotFoundException, BadArgumentException {
    O object = getByUUID(uuid);
    Optional<D> maybeDependentObject = getDependentObjects(object).findAny();
    if (maybeDependentObject.isPresent()) {
      throw new BadArgumentException(String.format(
          "%s with %s = %s depends on %s with %s = %s. %s cannot be deleted",
          getDependentObjectClass().getSimpleName(),
          getDependentObjectUniqueFieldName(),
          maybeDependentObject.get().getUniqueField(),
          getClassName(),
          getUniqueFieldName(),
          object.getUniqueField(),
          getClassName()
      ));
    }
    super.delete(uuid);
  }
  
  private Stream<D> getDependentObjects(O object) throws DatastoreException {
    return dependencyDatastore.findAll()
        .filter(d -> dependencyAppliesToObject(d, object));
  }
  
  protected List<String> replaceStringInList(List<String> strings, String oldValue, String newValue) {
    List<String> output = new ArrayList<>(strings);
    output.remove(oldValue);
    output.add(newValue);
    return output;
  }
  
  protected String replaceString(String truth, String oldValue, String newValue) {
    if (oldValue.equals(truth)) {
      return newValue;
    }
    return oldValue;
  }

}
