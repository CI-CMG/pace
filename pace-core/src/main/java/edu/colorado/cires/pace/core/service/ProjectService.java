package edu.colorado.cires.pace.core.service;

import edu.colorado.cires.pace.core.repository.CRUDRepository;
import edu.colorado.cires.pace.data.Project;
import java.util.function.Consumer;

public class ProjectService extends CRUDService<Project, String> {

  protected ProjectService(CRUDRepository<Project, String> projectRepository,
      Consumer<Project> onSuccess, Consumer<Exception> onFailure) {
    super(projectRepository, onSuccess, onFailure);
  }
}
