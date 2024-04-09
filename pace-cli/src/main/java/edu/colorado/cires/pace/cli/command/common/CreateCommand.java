package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.ObjectWithUniqueField;

public abstract class CreateCommand<O extends ObjectWithUniqueField> extends JsonBlobCommand<O> {

  @Override
  protected O runCommandWithDeserializedObject(O object) throws Exception {
    return createController().create(object);
  }
}
