package edu.colorado.cires.pace.cli.command.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;
import java.util.List;
import java.util.UUID;

public class ProjectCommandTest extends TranslateCommandTest<Project, ProjectTranslator> {

  @Override
  public Project createObject(String uniqueField, boolean withUUID) {
    return Project.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getRepositoryDirectory() {
    return "projects";
  }

  @Override
  protected String getCommandPrefix() {
    return "project";
  }

  @Override
  protected TypeReference<List<Project>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Project> getClazz() {
    return Project.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Project expected, Project actual, boolean checkUUID) {
    assertProjectsEqual(expected, actual, checkUUID);
  }

  public static void assertProjectsEqual(Project expected, Project actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(Project object) {
    return object.getName();
  }

  @Override
  protected Project updateObject(Project original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "projectName"
    };
  }

  @Override
  protected ProjectTranslator createTranslator(String name) {
    return ProjectTranslator.builder()
        .name(name)
        .projectUUID("UUID")
        .projectName("projectName")
        .build();
  }

  @Override
  protected String[] objectToRow(Project object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(),
        object.getName()
    };
  }

  @Override
  protected Translator createInvalidTranslator(String name) {
    return SeaTranslator.builder()
        .name(name)
        .seaUUID("UUID")
        .seaName("NAME")
        .build();
  }
}
