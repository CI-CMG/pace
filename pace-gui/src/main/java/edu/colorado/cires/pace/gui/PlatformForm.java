package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.platform.Platform;
import java.util.UUID;

/**
 * PlatformForm extends ObjectWithNameForm and provides
 * structure relevant to platform forms
 */
public class PlatformForm extends ObjectWithNameForm<Platform> {

  /**
   * Creates a platform form
   * @param initialObject object to build upon
   */
  public PlatformForm(Platform initialObject) {
    super(initialObject);
  }

  @Override
  protected Platform objectFromFormFields(UUID uuid, String uniqueField, boolean visible) {
    return Platform.builder()
        .uuid(uuid)
        .name(uniqueField)
        .visible(visible)
        .build();
  }
}
