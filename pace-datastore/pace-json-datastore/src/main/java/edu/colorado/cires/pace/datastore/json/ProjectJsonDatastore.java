package edu.colorado.cires.pace.datastore.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Project;
import java.io.IOException;
import java.nio.file.Path;

public class ProjectJsonDatastore extends JsonDatastore<Project> {

  public ProjectJsonDatastore(Path workDirectory, ObjectMapper objectMapper) throws IOException {
    super(workDirectory.resolve("projects"), objectMapper, Project.class);
  }

  @Override
  public String getUniqueFieldName() {
    return "name";
  }
}
