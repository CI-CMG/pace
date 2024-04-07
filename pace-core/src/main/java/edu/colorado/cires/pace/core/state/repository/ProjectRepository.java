package edu.colorado.cires.pace.core.state.repository;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.data.Project;

public class ProjectRepository extends CRUDRepository<Project, String> {

  public ProjectRepository(Datastore<Project, String> datastore) {
    super(Project::getUUID, Project::getName, Project::setUUID, datastore);
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
