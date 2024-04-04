package edu.colorado.cires.pace.cli.command.common;

public abstract class VoidCommand<O, U> extends CRUDCommand<O, U> {

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
