package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.project.Project;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;
import edu.colorado.cires.pace.translator.ValueWithColumnNumber;
import java.util.Map;

/**
 * ProjectConverter extends Converter and provides convert function for
 * Project objects
 */
public class ProjectConverter extends Converter<ProjectTranslator, Project> {

  /**
   * Creates a project object from the provided properties
   * @param translator translates to project object
   * @param properties maps property names to values
   * @param row relevant row
   * @param runtimeException thrown in case of error mapping
   * @return Project object
   */
  @Override
  public Project convert(ProjectTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Project.builder()
        .uuid(uuidFromMap(properties, "UUID", translator.getProjectUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getProjectName()))
        .build();
  }
}
