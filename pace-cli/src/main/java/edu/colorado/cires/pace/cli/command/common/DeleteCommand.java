package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class DeleteCommand<O extends ObjectWithUniqueField> extends VoidCommand<O> {

  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected void runVoidCommand() throws Exception {
    createRepository().delete(
        getUUIDProvider().get()
    );
  }
}
