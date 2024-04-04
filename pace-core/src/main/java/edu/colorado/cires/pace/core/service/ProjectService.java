package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Project;

public class ProjectService extends CRUDService<Project, String> {

  public ProjectService(CRUDRepository<Project, String> projectRepository) {
    super(projectRepository);
  }
}
