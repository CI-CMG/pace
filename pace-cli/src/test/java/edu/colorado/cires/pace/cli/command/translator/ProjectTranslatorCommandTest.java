package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.ProjectTranslator;

class ProjectTranslatorCommandTest extends TranslatorCommandTest<ProjectTranslator> {

  @Override
  protected void assertTranslatorTypeSpecificFields(ProjectTranslator expected, ProjectTranslator actual) {
    assertEquals(expected.getProjectUUID(), actual.getProjectUUID());
    assertEquals(expected.getProjectName(), actual.getProjectName());
  }

  @Override
  public ProjectTranslator createObject(String uniqueField) {
    return ProjectTranslator.builder()
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
