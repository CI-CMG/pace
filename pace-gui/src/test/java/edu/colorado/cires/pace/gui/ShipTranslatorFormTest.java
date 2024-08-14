package edu.colorado.cires.pace.gui;

import static org.junit.Assert.assertEquals;

import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;

public class ShipTranslatorFormTest extends SimpleObjectTranslatorFormTest<ShipTranslator, ShipTranslatorForm> {

  @Override
  protected ShipTranslatorForm createForm(ShipTranslator shipTranslator, String[] headerOptions) {
    return new ShipTranslatorForm(shipTranslator, headerOptions);
  }

  @Override
  protected void assertTranslatorEqualsHeaderOptions(ShipTranslator translator) {
    assertEquals("UUID", translator.getShipUUID());
    assertEquals("Name", translator.getShipName());
  }
}
