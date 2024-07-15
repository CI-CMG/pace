package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.io.IOException;

public abstract class FindAllCommand<O extends ObjectWithUniqueField> extends CRUDCommand<O> {
  
  protected abstract SearchParameters<O> getSearchParameters();

  @Override
  protected Object runCommand() throws IOException, DatastoreException {
    return createRepository().search(getSearchParameters())
        .toList();
  }

}
