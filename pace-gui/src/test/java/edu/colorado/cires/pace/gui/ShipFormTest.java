package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ship.Ship;
import java.util.UUID;

class ShipFormTest extends ObjectWithNameFormTest<Ship, ShipForm> {

  @Override
  protected ShipForm createMetadataForm(Ship initialObject) {
    return new ShipForm(initialObject);
  }

  @Override
  protected Ship createObject() {
    return Ship.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .visible(true)
        .build();
  }
}