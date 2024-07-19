package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class InstrumentTest extends ObjectWithUniqueFieldTest<Instrument> {

  @Override
  protected Instrument createObject() {
    return Instrument.builder().build();
  }
}