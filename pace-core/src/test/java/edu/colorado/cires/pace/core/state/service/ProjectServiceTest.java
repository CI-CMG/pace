package edu.colorado.cires.pace.core.state.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.core.state.repository.ProjectRepository;
import edu.colorado.cires.pace.data.Project;
import java.util.UUID;

class ProjectServiceTest extends CrudServiceTest<Project, ProjectRepository> {

  @Override
  protected Class<ProjectRepository> getRepositoryClass() {
    return ProjectRepository.class;
  }

  @Override
  protected CRUDService<Project> createService(ProjectRepository repository) {
    return new ProjectService(repository);
  }

  @Override
  protected Project createNewObject() {
    return new Project(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual) {
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.uuid(), actual.uuid());
  }
}
