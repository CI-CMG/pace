package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.instrument.Instrument;

class InstrumentTest extends ObjectWithUniqueFieldTest<Instrument> {

  @Override
  protected Instrument createObject() {
    return Instrument.builder().build();
  }
}