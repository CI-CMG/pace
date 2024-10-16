package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.project.Project;
import java.util.UUID;

/**
 * ProjectForm extends ObjectWithNameForm and provides structure
 * relevant to project forms
 */
public class ProjectForm extends ObjectWithNameForm<Project> {

  /**
   * Creates a project form
   * @param initialObject object to build upon
   */
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
