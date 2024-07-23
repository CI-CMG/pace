package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.Ship;
import java.util.UUID;

public class ShipForm extends ObjectWithNameForm<Ship> {

  public ShipForm(Ship initialObject) {
    super(initialObject);
  }

  @Override
  protected Ship objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return Ship.builder()
        .uuid(uuid)
        .name(uniqueField)
        .visible(visible)
        .build();
  }
}
