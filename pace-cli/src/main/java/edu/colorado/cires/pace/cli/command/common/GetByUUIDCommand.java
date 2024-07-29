package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;

public abstract class GetByUUIDCommand<O extends AbstractObject> extends CRUDCommand<O> {
  
  protected abstract Supplier<UUID> getUUIDProvider();

  @Override
  protected O runCommand() throws IOException, NotFoundException, DatastoreException, BadArgumentException {
    return createRepository().getByUUID(
        getUUIDProvider().get()
    );
  }
}
