package edu.colorado.cires.pace.cli.command.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import java.io.IOException;
import java.nio.file.Path;

public final class ProjectRepositoryFactory {

  public static ProjectRepository createRepository(Path datastoreDirectory, ObjectMapper objectMapper) throws IOException {
    return new ProjectRepository(
        new ProjectJsonDatastore(datastoreDirectory, objectMapper)
    );
  }

}
