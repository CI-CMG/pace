package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.project.translator.ProjectTranslator;

class ProjectTranslatorTest extends ObjectWithUniqueFieldTest<ProjectTranslator> {

  @Override
  protected ProjectTranslator createObject() {
    return ProjectTranslator.builder().build();
  }
}