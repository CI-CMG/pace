package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.ProjectRepository;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Project;
import java.util.UUID;

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
  protected CRUDService<Project, String> createService(ProjectRepository repository) {
    return new ProjectService(repository);
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
