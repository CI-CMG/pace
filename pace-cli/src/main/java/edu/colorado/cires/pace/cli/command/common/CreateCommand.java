package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.data.object.base.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.IOException;

public abstract class CreateCommand<O extends AbstractObject> extends JsonBlobCommand<O> {

  @Override
  protected O runCommandWithDeserializedObject(O object)
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException {
    return createRepository().create(object);
  }
}
