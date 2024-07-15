package edu.colorado.cires.pace.repository.search;

import edu.colorado.cires.pace.data.object.ObjectWithUniqueField;

public interface SearchParameters<O extends ObjectWithUniqueField> {
  
  boolean matches(O object);

}
