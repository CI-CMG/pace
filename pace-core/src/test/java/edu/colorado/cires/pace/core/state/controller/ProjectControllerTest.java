package edu.colorado.cires.pace.core.state.controller;

import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.core.state.service.CRUDService;
import edu.colorado.cires.pace.data.Project;
import java.util.UUID;
import java.util.function.Supplier;

class ProjectControllerTest extends CRUDControllerTest<Project, String> {

  @Override
  protected CRUDController<Project, String> createController(CRUDService<Project, String> service) {
    return new ProjectController(service);
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
  protected UniqueFieldSetter<Project, String> getUniqueFieldSetter() {
    return Project::setName;
  }

  @Override
  protected Supplier<String> getUniqueFieldName() {
    return () -> "name";
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
