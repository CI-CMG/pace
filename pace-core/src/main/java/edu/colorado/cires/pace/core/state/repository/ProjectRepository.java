package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Project;

public class ProjectRepository extends CRUDRepository<Project> {

  public ProjectRepository(Datastore<Project> datastore) {
    super(datastore);
  }

  @Override
  protected String getObjectName() {
    return Project.class.getSimpleName();
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }
}
