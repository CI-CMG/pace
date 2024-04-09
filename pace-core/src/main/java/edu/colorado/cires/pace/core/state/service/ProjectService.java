package edu.colorado.cires.pace.core.state.service;

import edu.colorado.cires.pace.core.state.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Project;

public class ProjectService extends CRUDService<Project> {

  public ProjectService(CRUDRepository<Project> projectRepository) {
    super(projectRepository);
  }
}
