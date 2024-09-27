package edu.colorado.cires.pace.datastore;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Datastores contain all objects in PACE and provide the ability to save, delete,
 * and search within them
 *
 * @param <O> Type of datastore
 */
public interface Datastore<O extends AbstractObject> {

  O save(O object) throws DatastoreException;
  void delete(O object) throws DatastoreException;
  Optional<O> findByUUID(UUID uuid) throws DatastoreException;
  Optional<O> findByUniqueField(String uniqueField) throws DatastoreException;
  Stream<O> findAll() throws DatastoreException;
  String getUniqueFieldName();
  String getClassName();
}
