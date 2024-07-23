package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Sea;
import java.util.UUID;

class SeaFormTest extends ObjectWithNameFormTest<Sea, SeaForm> {

  @Override
  protected SeaForm createMetadataForm(Sea initialObject) {
    return new SeaForm(initialObject);
  }

  @Override
  protected Sea createObject() {
    return Sea.builder()
        .uuid(UUID.randomUUID())
        .name("name")
        .visible(true)
        .build();
  }
}