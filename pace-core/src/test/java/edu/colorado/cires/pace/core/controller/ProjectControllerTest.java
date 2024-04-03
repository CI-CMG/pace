package edu.colorado.cires.pace.core.controller;

import edu.colorado.cires.pace.core.controller.validation.ConstraintViolation;
import edu.colorado.cires.pace.core.controller.validation.Validator;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.service.CRUDService;
import edu.colorado.cires.pace.data.Project;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

class ProjectControllerTest extends CRUDControllerTest<Project, String> {

  @Override
  protected CRUDController<Project, String> createController(CRUDService<Project, String> service, Validator<Project> validator,
      Consumer<Set<ConstraintViolation>> onValidationErrorHandler) {
    return new ProjectController(service, validator, onValidationErrorHandler);
  }

  @Override
  protected UniqueFieldProvider<Project, String> getUniqueFieldProvider() {
    return Project::getName;
  }

  @Override
  protected UUIDProvider<Project> getUuidProvider() {
    return Project::getUUID;
  }

  @Override
  protected Project createNewObject() {
    Project project = new Project();
    project.setName(UUID.randomUUID().toString());
    project.setUUID(UUID.randomUUID());
    project.setUse(true);
    return project;
  }
}
