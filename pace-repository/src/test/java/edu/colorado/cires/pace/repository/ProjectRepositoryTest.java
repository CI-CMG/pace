package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.repository.search.ProjectSearchParameters;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.util.List;
import java.util.function.Function;

class ProjectRepositoryTest extends CrudRepositoryTest<Project> {

  @Override
  protected CRUDRepository<Project> createRepository() {
    return new ProjectRepository(createDatastore());
  }

  @Override
  protected Function<Project, String> uniqueFieldGetter() {
    return Project::getName;
  }

  @Override
  protected SearchParameters<Project> createSearchParameters(List<Project> objects) {
    return ProjectSearchParameters.builder()
        .names(objects.stream().map(Project::getName).toList())
        .build();
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
