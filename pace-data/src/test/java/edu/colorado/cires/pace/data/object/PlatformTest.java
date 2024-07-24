package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class PlatformTest extends ObjectWithUniqueFieldTest<Platform> {

  @Override
  protected Platform createObject() {
    return Platform.builder().build();
  }
}