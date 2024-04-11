package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class DetectionTypeRepository extends CRUDRepository<DetectionType> {

  public DetectionTypeRepository(Datastore<DetectionType> datastore) {
    super(datastore);
  }

  @Override
  protected DetectionType setUUID(DetectionType object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
