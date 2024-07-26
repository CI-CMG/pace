package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.sea.Sea;

class SeaTest extends ObjectWithUniqueFieldTest<Sea> {

  @Override
  protected Sea createObject() {
    return Sea.builder().build();
  }
}