package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.ship.Ship;
import java.util.UUID;

/**
 * ShipForm extends ObjectWithNameForm and provides structure relevant
 * to ship forms
 */
public class ShipForm extends ObjectWithNameForm<Ship> {

  /**
   * Creates ship form
   * @param initialObject object to build upon
   */
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
