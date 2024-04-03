package edu.colorado.cires.pace.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.Project;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    return new ProjectRepository() {
      @Override
      public Stream<Project> findAll() {
        return findAllObjects();
      }

      @Override
      protected Project save(Project object) {
        return saveObject(object);
      }

      @Override
      protected Project delete(Project object) {
        return deleteObject(object);
      }

      @Override
      protected Optional<Project> findByUUID(UUID uuid) {
        return findObjectByUUID(uuid);
      }

      @Override
      protected Optional<Project> findByUniqueField(String uniqueField) {
        return findObjectByUniqueField(uniqueField);
      }
    };
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
