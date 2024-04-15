package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;

abstract class CreateCommand<O extends ObjectWithUniqueField> extends JsonBlobCommand<O> {

  @Override
  protected O runCommandWithDeserializedObject(O object) throws Exception {
    return createRepository().create(object);
  }
}
