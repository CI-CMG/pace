package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Sea;
import java.util.List;

class SeasPanelTest extends ObjectWithNamePanelTest<Sea> {

  @Override
  protected String getJsonFileName() {
    return "seas.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Sea Areas";
  }

  @Override
  protected String getMetadataPanelName() {
    return "seaAreasPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "seaForm";
  }

  @Override
  protected Sea createObject(String uniqueField) {
    return Sea.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected Sea updateObject(Sea original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected TypeReference<List<Sea>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Sea> getObjectClass() {
    return Sea.class;
  }

  @Override
  protected String getTranslatorPanelName() {
    return "seaTranslatorForm";
  }

  @Override
  protected String getTranslatorTypeName() {
    return "Sea Area";
  }
}
