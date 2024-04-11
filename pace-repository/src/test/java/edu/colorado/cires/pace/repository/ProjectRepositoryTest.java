package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.validation.ValidationException;

class ProjectRepositoryTest extends CrudRepositoryTest<Project> {

  @Override
  protected CRUDRepository<Project> createRepository() {
    return new ProjectRepository(createDatastore());
  }

  @Override
  protected Project createNewObject(int suffix) throws ValidationException {
    return Project.builder()
        .name(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Project copyWithUpdatedUniqueField(Project object, String uniqueField) throws ValidationException {
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
