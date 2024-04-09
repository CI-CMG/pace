package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.datastore.Datastore;
import edu.colorado.cires.pace.core.state.repository.ProjectRepository;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.state.service.ProjectService;
import edu.colorado.cires.pace.core.validation.ProjectValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Project;

public class ProjectController extends CRUDController<Project> {

  @Override
  protected Validator<Project> getValidator() {
    return new ProjectValidator();
  }

  @Override
  protected CRUDService<Project> createService(Datastore<Project> datastore, Datastore<?>... additionalDataStores) {
    return new ProjectService(
        new ProjectRepository(
            datastore
        )
    );
  }

  public ProjectController(Datastore<Project> datastore) {
    super(datastore);
  }
}
