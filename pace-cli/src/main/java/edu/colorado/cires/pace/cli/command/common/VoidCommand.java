package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.base.AbstractObject;

abstract class VoidCommand<O extends AbstractObject> extends CRUDCommand<O> {

  protected abstract void runVoidCommand() throws Exception;

  @Override
  protected Object runCommand() throws Exception {
    runVoidCommand();
    return null;
  }
}
