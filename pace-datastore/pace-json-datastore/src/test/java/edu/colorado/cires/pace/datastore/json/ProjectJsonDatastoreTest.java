package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.core.state.repository.UUIDProvider;
import edu.colorado.cires.pace.core.state.repository.UniqueFieldProvider;
import edu.colorado.cires.pace.data.Project;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class ProjectJsonDatastoreTest extends JsonDatastoreTest<Project, String> {

  @Override
  protected Class<Project> getClazz() {
    return Project.class;
  }

  @Override
  protected JsonDatastore<Project, String> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new ProjectJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected UUIDProvider<Project> createUUIDProvider() {
    return Project::getUUID;
  }

  @Override
  protected UniqueFieldProvider<Project, String> createUniqueFieldProvider() {
    return Project::getName;
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
    assertEquals(expected.getUUID(), actual.getUUID());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getUse(), actual.getUse());
  }
}
