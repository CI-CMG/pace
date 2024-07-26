package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.project.Project;
import java.util.UUID;

class ProjectFormTest extends ObjectWithNameFormTest<Project, ProjectForm> {

  @Override
  protected ProjectForm createMetadataForm(Project initialObject) {
    return new ProjectForm(initialObject);
  }

  @Override
  protected Project createObject() {
    return Project.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .visible(true)
        .build();
  }
}