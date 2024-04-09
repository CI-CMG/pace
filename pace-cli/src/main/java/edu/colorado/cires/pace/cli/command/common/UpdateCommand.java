package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.util.UUID;

public abstract class UpdateCommand<O extends ObjectWithUniqueField> extends JsonBlobCommand<O> {

  @Override
  protected O runCommandWithDeserializedObject(O object) throws Exception {
    UUID uuid = object.uuid();
    return createController().update(uuid, object);
  }
}
