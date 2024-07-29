package edu.colorado.cires.pace.cli.command.common;

import edu.colorado.cires.pace.data.object.base.AbstractObject;
import edu.colorado.cires.pace.datastore.DatastoreException;
import edu.colorado.cires.pace.repository.search.SearchParameters;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FindAllCommand<O extends AbstractObject> extends CRUDCommand<O> {
  
  protected abstract List<String> getUniqueFields();
  protected abstract Boolean getShowHidden();
  protected abstract Boolean getShowVisible();
  
  private List<Boolean> getVisibilityStates() {
    List<Boolean> visibilityStates = new ArrayList<>(0);
    
    if (!getShowHidden() && !getShowVisible()) {
      visibilityStates.add(true); 
    } else {
      if (getShowHidden()) {
        visibilityStates.add(false);
      }
      if (getShowVisible()) {
        visibilityStates.add(true);
      }
    }
    
    return visibilityStates;
  }

  @Override
  protected Object runCommand() throws IOException, DatastoreException {
    return createRepository().search(SearchParameters.<O>builder()
            .uniqueFields(getUniqueFields())
            .visibilityStates(getVisibilityStates())
            .build())
        .toList();
  }

}
