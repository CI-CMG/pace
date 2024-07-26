package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.project.Project;

class ProjectTest extends ObjectWithUniqueFieldTest<Project> {

  @Override
  protected Project createObject() {
    return Project.builder().build();
  }
}