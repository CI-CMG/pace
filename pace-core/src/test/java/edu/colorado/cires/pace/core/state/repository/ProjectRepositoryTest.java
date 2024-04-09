package edu.colorado.cires.pace.core.state.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Project;

class ProjectRepositoryTest extends CrudRepositoryTest<Project> {

  @Override
  protected CRUDRepository<Project> createRepository() {
    return new ProjectRepository(createDatastore());
  }

  @Override
  protected Project createNewObject(int suffix) {
    return new Project(
        null,
        String.format("name-%s", suffix)
    );
  }

  @Override
  protected Project copyWithUpdatedUniqueField(Project object, String uniqueField) {
    return new Project(
        object.uuid(),
        uniqueField
    );
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual, boolean checkUUID) {
    assertEquals(expected.name(), actual.name());
    if (checkUUID) {
      assertEquals(expected.uuid(), actual.uuid());
    }
  }
}
