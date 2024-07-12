package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;

abstract class VoidCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {

  protected abstract void runVoidCommand() throws Exception;

  @Override
  protected Object runCommand() throws Exception {
    runVoidCommand();
    return null;
  }
}
