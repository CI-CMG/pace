package edu.colorado.cires.pace.cli.command.common;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class GetByUUIDCommand<O, U> extends CRUDCommand<O, U> {
  
  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected O runCommand() throws Exception {
    return createController().getByUUID(
        getUUIDProvider().get()
    );
  }
}
