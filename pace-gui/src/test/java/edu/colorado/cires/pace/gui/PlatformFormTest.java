package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.platform.Platform;
import java.util.UUID;

class PlatformFormTest extends ObjectWithNameFormTest<Platform, PlatformForm> {

  @Override
  protected PlatformForm createMetadataForm(Platform initialObject) {
    return new PlatformForm(initialObject);
  }

  @Override
  protected Platform createObject() {
    return Platform.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .visible(true)
        .build();
  }
}