package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.Sea;

class SeaTranslatorTest extends ObjectWithUniqueFieldTest<SeaTranslator> {

  @Override
  protected SeaTranslator createObject() {
    return SeaTranslator.builder().build();
  }
}