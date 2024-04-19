package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;

abstract class GetByUUIDCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected O runCommand() throws IOException, NotFoundException, DatastoreException {
    return createRepository().getByUUID(
        getUUIDProvider().get()
    );
  }
}
