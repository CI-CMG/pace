package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.sea.Sea;
import java.util.UUID;

public class SeaForm extends ObjectWithNameForm<Sea> {

  public SeaForm(Sea initialObject) {
    super(initialObject);
  }

  @Override
  protected Sea objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return Sea.builder()
        .uuid(uuid)
        .name(uniqueField)
        .visible(visible)
        .build();
  }
}
