package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.project.Project;
import java.io.IOException;
import java.nio.file.Path;

/**
 * ProjectJsonDatastore extends JsonDatastore and returns name
 * as the unique field name
 */
public class ProjectJsonDatastore extends JsonDatastore<Project> {

  /**
   * Initializes project JSON datastore
   *
   * @param workDirectory location of datastore
   * @param objectMapper relevant object mapper
   * @throws IOException in case of input or output error
   */
  public ProjectJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("projects"), objectMapper, Project.class, Project::getName);
  }

  /**
   * Returns the unique field name
   * @return String name
   */
  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
