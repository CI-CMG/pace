package edu.colorado.cires.pace.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.repository.ProjectRepository;
import edu.colorado.cires.pace.core.repository.UUIDProvider;
import edu.colorado.cires.pace.core.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Project;
import java.util.UUID;
import java.util.function.Consumer;

class ProjectServiceTest extends CrudServiceTest<Project, String, ProjectRepository> {

  @Override
  protected Class<ProjectRepository> getRepositoryClass() {
    return ProjectRepository.class;
  }

  @Override
  protected UniqueFieldProvider<Project, String> getUniqueFieldProvider() {
    return Project::getName;
  }

  @Override
  protected UUIDProvider<Project> getUUIDProvider() {
    return Project::getUUID;
  }

  @Override
  protected CRUDService<Project, String> createService(ProjectRepository repository, Consumer<Project> onSuccessHandler,
      Consumer<Exception> onFailureHandler) {
    return new ProjectService(repository, onSuccessHandler, onFailureHandler);
  }

  @Override
  protected Project createNewObject() {
    Project project = new Project();
    project.setUUID(UUID.randomUUID());
    project.setName(UUID.randomUUID().toString());
    project.setUse(true);
    return project;
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual) {
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
    assertEquals(expected.getUUID(), actual.getUUID());
  }
}
