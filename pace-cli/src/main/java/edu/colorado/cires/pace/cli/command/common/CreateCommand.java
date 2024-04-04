package edu.colorado.cires.pace.cli.command.common;

public abstract class CreateCommand<O, U> extends JsonBlobCommand<O, U> {

  @Override
  protected O runCommandWithDeserializedObject(O object) throws Exception {
    return createController().create(object);
  }
}
