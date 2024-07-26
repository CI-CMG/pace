package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.project.Project;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class ProjectJsonDatastoreTest extends JsonDatastoreTest<Project> {

  @Override
  protected Class<Project> getClazz() {
    return Project.class;
  }

  @Override
  protected JsonDatastore<Project> createDatastore(Path storagePath, ObjectMapper objectMapper) throws IOException {
    return new ProjectJsonDatastore(storagePath, objectMapper);
  }

  @Override
  protected String getExpectedUniqueFieldName() {
    return "name";
  }

  @Override
  protected Project createNewObject(int suffix) {
    return Project.builder()
        .uuid(UUID.randomUUID())
        .name(UUID.randomUUID().toString())
        .build();
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual) {
    assertEquals(expected.getUuid(), actual.getUuid());
    assertEquals(expected.getName(), actual.getName());
  }
}
