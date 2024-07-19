package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.Instrument;

class InstrumentTranslatorTest extends ObjectWithUniqueFieldTest<InstrumentTranslator> {

  @Override
  protected InstrumentTranslator createObject() {
    return InstrumentTranslator.builder().build();
  }
}