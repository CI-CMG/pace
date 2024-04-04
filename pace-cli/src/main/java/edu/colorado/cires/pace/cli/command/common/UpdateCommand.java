package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.core.repository.UUIDProvider;
import java.util.UUID;

public abstract class UpdateCommand<O, U> extends JsonBlobCommand<O, U> {
  
  protected abstract UUIDProvider<O> getUUIDProvider();

  @Override
  protected O runCommandWithDeserializedObject(O object) throws Exception {
    UUID uuid = getUUIDProvider().getUUID(object);
    return createController().update(uuid, object);
  }
}
