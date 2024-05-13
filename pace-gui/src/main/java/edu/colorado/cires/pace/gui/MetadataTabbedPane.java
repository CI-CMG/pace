package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.datastore.json.ProjectJsonDatastore;
import edu.colorado.cires.pace.repository.ProjectRepository;
import edu.colorado.cires.pace.utilities.ApplicationPropertyResolver;
import edu.colorado.cires.pace.utilities.SerializationUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import javax.swing.JTabbedPane;

public class MetadataTabbedPane extends JTabbedPane {
  
  private final Path workDir = new ApplicationPropertyResolver().getWorkDir();
  private final ObjectMapper objectMapper = SerializationUtils.createObjectMapper();

  public MetadataTabbedPane() throws IOException {
    add("Projects", new DataPanel<>(new ProjectRepository(
        new ProjectJsonDatastore(
            workDir, objectMapper
        )
    ), new String[]{
        "UUID", "Name"
    }, (project -> new Object[] {
        project.getUuid(), project.getName()
    }), (o) -> Project.builder()
        .uuid((UUID) o[0])
        .name((String) o[1])
        .build(), ProjectForm::new));
  }
}
