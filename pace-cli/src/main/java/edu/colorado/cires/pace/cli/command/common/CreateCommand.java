package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.BadArgumentException;
import edu.colorado.cires.pace.repository.ConflictException;
import edu.colorado.cires.pace.repository.NotFoundException;
import java.io.IOException;

abstract class CreateCommand<O extends ObjectWithUniqueField> extends JsonBlobCommand<O> {

  @Override
  protected O runCommandWithDeserializedObject(O object)
      throws IOException, ConflictException, NotFoundException, DatastoreException, BadArgumentException {
    return createRepository().create(object);
  }
}
