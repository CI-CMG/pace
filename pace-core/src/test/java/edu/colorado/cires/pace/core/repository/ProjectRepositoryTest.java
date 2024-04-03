package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Project;

class ProjectRepositoryTest extends CrudRepositoryTest<Project, String> {


  @Override
  protected UUIDProvider<Project> getUUIDPRovider() {
    return Project::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Project, String> getUniqueFieldProvider() {
    return Project::getName;
  }

  @Override
  protected UUIDSetter<Project> getUUIDSetter() {
    return Project::setUUID;
  }

  @Override
  protected UniqueFieldSetter<Project, String> getUniqueFieldSetter() {
    return Project::setName;
  }

  @Override
  protected CRUDRepository<Project, String> createRepository() {
    return new ProjectRepository(createDatastore());
  }

  @Override
  protected Project createNewObject(int suffix) {
    Project project = new Project();
    project.setName(String.format("name-%s", suffix));
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
