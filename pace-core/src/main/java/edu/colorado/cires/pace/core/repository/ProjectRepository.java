package edu.colorado.cires.pace.core.repository;

import edu.colorado.cires.pace.data.Project;

public abstract class ProjectRepository extends CRUDRepository<Project, String> {

  protected ProjectRepository() {
    super(Project::getUUID, Project::getName, Project::setUUID);
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
