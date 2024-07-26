package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.project.Project;
import java.util.UUID;

public class ProjectForm extends ObjectWithNameForm<Project> {


  public ProjectForm(Project initialObject) {
    super(initialObject);
  }

  @Override
  protected Project objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return Project.builder()
        .uuid(uuid)
        .name(uniqueField)
        .visible(visible)
        .build();
  }
}
