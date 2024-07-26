package edu.colorado.cires.pace.data.object;

import edu.colorado.cires.pace.data.ObjectWithUniqueFieldTest;
import edu.colorado.cires.pace.data.object.ship.Ship;

class ShipTest extends ObjectWithUniqueFieldTest<Ship> {

  @Override
  protected Ship createObject() {
    return Ship.builder().build();
  }
}