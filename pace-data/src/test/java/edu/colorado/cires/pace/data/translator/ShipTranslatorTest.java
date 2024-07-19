package edu.colorado.cires.pace.data.translator;

import static org.junit.jupiter.api.Assertions.*;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;

class ShipTranslatorTest extends ObjectWithUniqueFieldTest<ShipTranslator> {

  @Override
  protected ShipTranslator createObject() {
    return ShipTranslator.builder().build();
  }
}