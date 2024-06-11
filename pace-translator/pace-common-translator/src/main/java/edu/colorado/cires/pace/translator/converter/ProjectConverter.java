package edu.colorado.cires.pace.translator.converter;

import static edu.colorado.cires.pace.translator.converter.ConversionUtils.stringFromMap;
import static edu.colorado.cires.pace.translator.converter.ConversionUtils.uuidFromMap;

import edu.colorado.cires.pace.data.object.Project;
import edu.colorado.cires.pace.data.translator.ProjectTranslator;
import edu.colorado.cires.pace.translator.TranslatorExecutor.ValueWithColumnNumber;
import java.util.Map;

public class ProjectConverter extends Converter<ProjectTranslator, Project> {

  @Override
  public Project convert(ProjectTranslator translator, Map<String, ValueWithColumnNumber> properties, int row, RuntimeException runtimeException) {
    return Project.builder()
        .uuid(uuidFromMap(properties, translator.getProjectUUID(), row, runtimeException))
        .name(stringFromMap(properties, translator.getProjectName()))
        .build();
  }
}
