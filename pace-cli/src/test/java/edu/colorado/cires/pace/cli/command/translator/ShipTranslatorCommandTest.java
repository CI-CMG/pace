package edu.colorado.cires.pace.cli.command.translator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.translator.ShipTranslator;
import java.util.UUID;

class ShipTranslatorCommandTest extends TranslatorCommandTest<ShipTranslator> {

  @Override
  public ShipTranslator createObject(String uniqueField, boolean withUUID) {
    return ShipTranslator.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .shipName("shipName")
        .shipUUID("shipUUID")
        .build();
  }

  @Override
  protected ShipTranslator updateObject(ShipTranslator original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertTranslatorTypeSpecificFields(ShipTranslator expected, ShipTranslator actual) {
    assertEquals(expected.getShipUUID(), actual.getShipUUID());
    assertEquals(expected.getShipName(), actual.getShipName());
  }
}
