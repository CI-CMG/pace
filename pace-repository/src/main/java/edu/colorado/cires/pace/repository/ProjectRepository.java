package edu.colorado.cires.pace.repository;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.validation.ValidationException;
import edu.colorado.cires.pace.datastore.Datastore;
import java.util.UUID;

public class ProjectRepository extends CRUDRepository<Project> {

  public ProjectRepository(Datastore<Project> datastore) {
    super(datastore);
  }

  @Override
  protected Project setUUID(Project object, UUID uuid) throws ValidationException {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
