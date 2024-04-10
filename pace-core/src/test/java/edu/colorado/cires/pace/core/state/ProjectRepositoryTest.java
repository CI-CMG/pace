package edu.colorado.cires.pace.core.state;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Project;

class ProjectRepositoryTest extends CrudRepositoryTest<Project> {

  @Override
  protected CRUDRepository<Project> createRepository() {
    return new ProjectRepository(createDatastore());
  }

  @Override
  protected Project createNewObject(int suffix) {
    return Project.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Project copyWithUpdatedUniqueField(Project object, String uniqueField) {
    return Project.builder()
        .uuid(object.getUuid())
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
  }
}
