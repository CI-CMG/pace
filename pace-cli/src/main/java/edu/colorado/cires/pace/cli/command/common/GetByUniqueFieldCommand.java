package edu.colorado.cires.pace.cli.command.common;

import java.util.function.Supplier;

public abstract class GetByUniqueFieldCommand<O, U> extends CRUDCommand<O, U> {

  protected abstract Supplier<U> getUniqueFieldProvider();
  
  @Override
  protected O runCommand() throws Exception {
    return createController().getByUniqueField(
        getUniqueFieldProvider().get()
    );
  }
}
