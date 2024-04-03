package edu.colorado.cires.pace.core.datastore;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Datastore<O, U> {

  O save(O object);
  void delete(O object);
  Optional<O> findByUUID(UUID uuid);
  Optional<O> findByUniqueField(U uniqueField);
  Stream<O> findAll();
  
}
