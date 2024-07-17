package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Ship;
import java.util.List;

public class ShipsPanelTest extends ObjectWithNamePanelTest<Ship> {

  @Override
  protected String getTranslatorPanelName() {
    return "shipTranslatorForm";
  }

  @Override
  protected String getTranslatorTypeName() {
    return "Ship";
  }

  @Override
  protected String getJsonFileName() {
    return "ships.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Ships";
  }

  @Override
  protected String getMetadataPanelName() {
    return "shipsPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "shipForm";
  }

  @Override
  protected Ship createObject(String uniqueField) {
    return Ship.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected Ship updateObject(Ship original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected TypeReference<List<Ship>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Ship> getObjectClass() {
    return Ship.class;
  }
}
