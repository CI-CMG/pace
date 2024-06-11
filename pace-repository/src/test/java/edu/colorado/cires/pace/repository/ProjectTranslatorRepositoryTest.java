package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.translator.Translator;

public class ProjectTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return ProjectTranslator.builder()
        .name(String.format("name-%s", suffix))
        .projectUUID(String.format("uuid-%s", suffix))
        .projectName(String.format("project-name-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((ProjectTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    assertEquals(expected.getName(), actual.getName());
    
    ProjectTranslator expectedProjectTranslator = (ProjectTranslator) expected;
    ProjectTranslator actualProjectTranslator = (ProjectTranslator) actual;
    assertEquals(expectedProjectTranslator.getProjectUUID(), actualProjectTranslator.getProjectUUID());
    assertEquals(expectedProjectTranslator.getProjectName(), actualProjectTranslator.getProjectName());
  }
}
