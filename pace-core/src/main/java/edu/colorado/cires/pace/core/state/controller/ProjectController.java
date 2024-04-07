package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.core.validation.ProjectValidator;
import edu.colorado.cires.pace.core.validation.Validator;
import edu.colorado.cires.pace.data.Project;

public class ProjectController extends CRUDController<Project, String> {

  @Override
  protected Validator<Project> getValidator() {
    return new ProjectValidator();
  }

  public ProjectController(CRUDService<Project, String> service) {
    super(service);
  }
}
