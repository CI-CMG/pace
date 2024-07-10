package edu.colorado.cires.pace.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.colorado.cires.pace.data.object.Platform;
import java.util.List;

class PlatformsPanelTest extends ObjectWithNamePanelTest<Platform> {

  @Override
  protected String getJsonFileName() {
    return "platforms.json";
  }

  @Override
  protected String getMetadataTabTitle() {
    return "Platforms";
  }

  @Override
  protected String getMetadataPanelName() {
    return "platformsPanel";
  }

  @Override
  protected String getFormPanelName() {
    return "platformForm";
  }

  @Override
  protected Platform createObject(String uniqueField) {
    return Platform.builder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected Platform updateObject(Platform original, String uniqueField) {
    return original.toBuilder()
        .name(uniqueField)
        .build();
  }

  @Override
  protected TypeReference<List<Platform>> getTypeReference() {
    return new TypeReference<>() {};
  }

  @Override
  protected Class<Platform> getObjectClass() {
    return Platform.class;
  }
}
