package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.instrument.translator.InstrumentTranslator;

class InstrumentTranslatorTest extends ObjectWithUniqueFieldTest<InstrumentTranslator> {

  @Override
  protected InstrumentTranslator createObject() {
    return InstrumentTranslator.builder().build();
  }
}