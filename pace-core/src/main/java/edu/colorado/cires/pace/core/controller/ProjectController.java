package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Project;

public class ProjectController extends CRUDController<Project, String> {

  protected ProjectController(CRUDService<Project, String> service,
      Validator<Project> validator) {
    super(service, validator);
  }
}
