package edu.colorado.cires.pace.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import edu.colorado.cires.pace.data.object.base.Translator;

public class ShipTranslatorRepositoryTest extends TranslatorRepositoryTest {

  @Override
  protected Translator createNewObject(int suffix) {
    return ShipTranslator.builder()
        .name(String.format("translator-name-%s", suffix))
        .shipUUID(String.format("uuid-%s", suffix))
        .shipName(String.format("name-%s", suffix))
        .build();
  }

  @Override
  protected Translator copyWithUpdatedUniqueField(Translator object, String uniqueField) {
    return ((ShipTranslator) object).toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected void assertObjectsEqual(Translator expected, Translator actual, boolean checkUUID) {
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    }
    
    assertEquals(expected.getName(), actual.getName());
    ShipTranslator expectedShipTranslator = (ShipTranslator) expected;
    ShipTranslator actualShipTranslator = (ShipTranslator) actual;
    assertEquals(expectedShipTranslator.getShipName(), actualShipTranslator.getShipName());
    assertEquals(expectedShipTranslator.getShipUUID(), actualShipTranslator.getShipUUID());
  }
}
