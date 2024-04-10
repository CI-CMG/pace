package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;

public abstract class VoidCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {

  protected abstract void runVoidCommand() throws Exception;
  
  @Override
  public void run() {
    try {
      runVoidCommand();
    } catch (Exception e) {
      throw new IllegalStateException("Command failed", e);
    }
  }

  @Override
  protected Object runCommand() {
    return null; // no-op
  }
}
