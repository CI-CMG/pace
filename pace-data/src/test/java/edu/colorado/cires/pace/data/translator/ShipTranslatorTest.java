package edu.colorado.cires.pace.data.translator;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;

class ShipTranslatorTest extends ObjectWithUniqueFieldTest<ShipTranslator> {

  @Override
  protected ShipTranslator createObject() {
    return ShipTranslator.builder().build();
  }
}