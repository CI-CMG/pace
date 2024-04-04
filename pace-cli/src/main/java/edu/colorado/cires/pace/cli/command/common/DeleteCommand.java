package edu.colorado.cires.pace.cli.command.common;

import java.util.UUID;
import java.util.function.Supplier;

public abstract class DeleteCommand<O, U> extends VoidCommand<O, U> {

  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected void runVoidCommand() throws Exception {
    createController().delete(
        getUUIDProvider().get()
    );
  }
}
