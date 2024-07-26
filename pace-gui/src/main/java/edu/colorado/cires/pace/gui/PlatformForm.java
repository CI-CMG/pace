package edu.colorado.cires.pace.gui;

import edu.colorado.cires.pace.data.object.platform.Platform;
import java.util.UUID;

public class PlatformForm extends ObjectWithNameForm<Platform> {

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
