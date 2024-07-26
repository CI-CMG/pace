package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.sea.translator.SeaTranslator;

class SeaTranslatorTest extends ObjectWithUniqueFieldTest<SeaTranslator> {

  @Override
  protected SeaTranslator createObject() {
    return SeaTranslator.builder().build();
  }
}