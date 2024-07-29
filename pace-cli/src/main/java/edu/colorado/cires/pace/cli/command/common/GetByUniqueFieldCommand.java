package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.IOException;
import java.util.function.Supplier;

public abstract class GetByUniqueFieldCommand<O extends AbstractObject> extends CRUDCommand<O> {

  protected abstract Supplier<String> getUniqueFieldProvider();
  
  @Override
  protected O runCommand() throws IOException, NotFoundException, DatastoreException {
    return createRepository().getByUniqueField(
        getUniqueFieldProvider().get()
    );
  }
}
