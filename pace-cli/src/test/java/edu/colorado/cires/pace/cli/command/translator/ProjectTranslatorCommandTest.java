package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import java.util.UUID;

class ProjectTranslatorCommandTest extends TranslatorCommandTest<ProjectTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(ProjectTranslator expected, ProjectTranslator actual) {
    assertEquals(expected.getProjectUUID(), actual.getProjectUUID());
    assertEquals(expected.getProjectName(), actual.getProjectName());
  }

  @Override
  public ProjectTranslator createObject(String uniqueField, boolean withUUID) {
    return ProjectTranslator.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .projectName("projectName")
        .projectUUID("projectUUID")
        .build();
  }

  @Override
  protected ProjectTranslator updateObject(ProjectTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }
}
