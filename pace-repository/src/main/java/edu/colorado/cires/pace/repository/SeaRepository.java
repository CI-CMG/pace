package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Sea;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class SeaRepository extends CRUDRepository<Sea> {

  public SeaRepository(Datastore<Sea> datastore) {
    super(datastore);
  }

  @Override
  protected Sea setUUID(Sea object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
