package edu.colorado.cires.pace.cli.command.ship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.cli.command.TranslateCommandTest;
import edu.colorado.cires.pace.data.object.ship.Ship;
import edu.colorado.cires.pace.data.object.ship.translator.ShipTranslator;
import java.util.List;
import java.util.UUID;

class ShipCommandTest extends TranslateCommandTest<Ship, ShipTranslator> {


  @Override
  public Ship createObject(String uniqueField, boolean withUUID) {
    return Ship.builder()
        .uuid(withUUID ? UUID.randomUUID() : null)
        .name(uniqueField)
        .build();
  }

  @Override
  protected String getRepositoryDirectory() {
    return "ships";
  }

  @Override
  protected String getCommandPrefix() {
    return "ship";
  }

  @Override
  protected TypeReference<List<Ship>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Ship> getClazz() {
    return Ship.class;
  }

  @Override
  protected String getUniqueFieldName() {
    return "name";
  }

  @Override
  protected void assertObjectsEqual(Ship expected, Ship actual, boolean checkUUID) {
    assertEquals(expected.getName(), actual.getName());
    if (checkUUID) {
      assertEquals(expected.getUuid(), actual.getUuid());
    } else {
      assertNotNull(actual.getUuid());
    }
  }

  @Override
  protected String getUniqueField(Ship object) {
    return object.getName();
  }

  @Override
  protected Ship updateObject(Ship original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected String[] getTranslatorFields() {
    return new String[] {
        "UUID",
        "shipName"
    };
  }

  @Override
  protected ShipTranslator createTranslator(String name) {
    return ShipTranslator.builder()
        .name(name)
        .shipUUID("UUID")
        .shipName("shipName")
        .build();
  }

  @Override
  protected String[] objectToRow(Ship object) {
    return new String[] {
        object.getUuid() == null ? "" : object.getUuid().toString(), 
        object.getName()
    };
  }
}