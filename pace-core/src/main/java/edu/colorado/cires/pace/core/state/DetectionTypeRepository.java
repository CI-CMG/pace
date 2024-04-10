package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.DetectionType;
import edu.colorado.cires.pace.data.validation.ValidationException;
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
