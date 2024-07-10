package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Project;
import java.util.List;

class ProjectPanelTest extends ObjectWithNamePanelTest<Project> {

  @Override
  protected String getJsonFileName() {
    return "projects.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Projects";
  }

  @Override
  protected String getMetadataPanelName() {
    return "projectsPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "projectForm";
  }

  @Override
  protected Project createObject(String uniqueField) {
    return Project.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected Project updateObject(Project original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected TypeReference<List<Project>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Project> getObjectClass() {
    return Project.class;
  }
}
