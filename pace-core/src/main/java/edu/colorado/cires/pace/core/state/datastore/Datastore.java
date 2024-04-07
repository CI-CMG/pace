package edu.colorado.cires.pace.core.state.datastore;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Datastore<O, U> {

  O save(O object) throws Exception;
  void delete(O object) throws Exception;
  Optional<O> findByUUID(UUID uuid) throws Exception;
  Optional<O> findByUniqueField(U uniqueField) throws Exception;
  Stream<O> findAll() throws Exception;
  
}
