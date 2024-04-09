package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.util.function.Supplier;

public abstract class GetByUniqueFieldCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {

  protected abstract Supplier<String> getUniqueFieldProvider();
  
  @Override
  protected O runCommand() throws Exception {
    return createController().getByUniqueField(
        getUniqueFieldProvider().get()
    );
  }
}
