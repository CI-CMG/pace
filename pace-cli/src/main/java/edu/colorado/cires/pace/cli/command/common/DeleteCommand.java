package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class DeleteCommand<O extends AbstractObject> extends VoidCommand<O> {

  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected void runVoidCommand() throws Exception {
    createRepository().delete(
        getUUIDProvider().get()
    );
  }
}
