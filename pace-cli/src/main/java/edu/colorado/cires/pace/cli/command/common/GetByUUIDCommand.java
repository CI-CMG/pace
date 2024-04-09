package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.ObjectWithUniqueField;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class GetByUUIDCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected O runCommand() throws Exception {
    return createController().getByUUID(
        getUUIDProvider().get()
    );
  }
}
