package edu.colorado.cires.pace.core.state;

import edu.colorado.cires.pace.data.object.Project;
import java.util.UUID;

public class ProjectRepository extends CRUDRepository<Project> {

  public ProjectRepository(Datastore<Project> datastore) {
    super(datastore);
  }

  @Override
  protected Project setUUID(Project object, UUID uuid) {
    return object.toBuilder()
        .uuid(uuid)
        .build();
  }

}
