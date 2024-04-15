package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import java.util.UUID;
import java.util.function.Supplier;

abstract class GetByUUIDCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected O runCommand() throws Exception {
    return createRepository().getByUUID(
        getUUIDProvider().get()
    );
  }
}
