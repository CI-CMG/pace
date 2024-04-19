package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import java.io.IOException;

abstract class FindAllCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {

  @Override
  protected Object runCommand() throws IOException, DatastoreException {
    return createRepository().findAll()
        .toList();
  }

}
