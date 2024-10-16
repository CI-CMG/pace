package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.sea.Sea;
import java.util.UUID;

/**
 * SeaForm extends ObjectWithNameForm
 */
public class SeaForm extends ObjectWithNameForm<Sea> {

  /**
   * Creates sea form
   * @param initialObject object to build upon
   */
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
