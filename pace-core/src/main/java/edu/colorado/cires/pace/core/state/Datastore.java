package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface Datastore<O extends ObjectWithUniqueField> {

  O save(O object) throws Exception;
  void delete(O object) throws Exception;
  Optional<O> findByUUID(UUID uuid) throws Exception;
  Optional<O> findByUniqueField(String uniqueField) throws Exception;
  Stream<O> findAll() throws Exception;
  String getUniqueFieldName();
  String getClassName();
  
}
