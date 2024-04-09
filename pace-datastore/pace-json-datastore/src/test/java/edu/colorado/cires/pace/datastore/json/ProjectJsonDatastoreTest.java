package edu.colorado.cires.pace.datastore.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.Project;
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
  protected Project createNewObject() {
    return new Project(
        UUID.randomUUID(),
        UUID.randomUUID().toString()
    );
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual) {
    assertEquals(expected.uuid(), actual.uuid());
    assertEquals(expected.name(), actual.name());
  }
}
