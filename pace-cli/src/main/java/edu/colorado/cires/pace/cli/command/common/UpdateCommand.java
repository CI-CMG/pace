package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;

public abstract class UpdateCommand<O extends ObjectWithUniqueField> extends JsonBlobCommand<O> {

  @Override
  protected O runCommandWithDeserializedObject(O object) throws Exception {
    UUID uuid = object.getUuid();
    return createRepository().update(uuid, object);
  }
}
