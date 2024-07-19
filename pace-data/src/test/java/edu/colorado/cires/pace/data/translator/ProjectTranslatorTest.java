package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.Project;

class ProjectTranslatorTest extends ObjectWithUniqueFieldTest<ProjectTranslator> {

  @Override
  protected ProjectTranslator createObject() {
    return ProjectTranslator.builder().build();
  }
}